package org.arcanum.pairing;

import org.arcanum.Pairing;
import org.arcanum.Parameters;
import org.arcanum.common.parameters.PropertiesParameters;
import org.arcanum.pairing.a.TypeAPairing;
import org.arcanum.pairing.a1.TypeA1Pairing;
import org.arcanum.pairing.d.TypeDPairing;
import org.arcanum.pairing.e.TypeEPairing;
import org.arcanum.pairing.f.TypeFPairing;
import org.arcanum.pairing.g.TypeGPairing;
import org.arcanum.pairing.immutable.ImmutableParing;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class PairingFactory {
    private static final PairingFactory INSTANCE = new PairingFactory();


    public static PairingFactory getInstance() {
        return INSTANCE;
    }

    public static Pairing getPairing(Parameters parameters) {
        return getInstance().initPairing(parameters);
    }

    public static Pairing getPairing(String parametersPath) {
        return getInstance().initPairing(parametersPath);
    }

    public static Pairing getPairing(Parameters parameters, SecureRandom random) {
        return getInstance().initPairing(parameters, random);
    }

    public static Pairing getPairing(String parametersPath, SecureRandom random) {
        return getInstance().initPairing(parametersPath, random);
    }

    public static Parameters getPairingParameters(String parametersPath) {
        return getInstance().loadParameters(parametersPath);
    }


    private boolean usePBCWhenPossible = false;
    private boolean reuseInstance = true;
    private boolean pbcAvailable = false;
    private boolean immutable = false;

    private Map<Parameters, Pairing> instances;
    private Map<String, PairingCreator> creators;
    private SecureRandomCreator secureRandomCreator;


    private PairingFactory() {
        this.instances = new HashMap<Parameters, Pairing>();
        this.creators = new HashMap<String, PairingCreator>();
        this.secureRandomCreator = new DefaultSecureRandomCreator();

        PairingCreator defaultCreator = new EllipticCurvesPairingCreator();
        creators.put("a", defaultCreator);
        creators.put("a1", defaultCreator);
        creators.put("d", defaultCreator);
        creators.put("e", defaultCreator);
        creators.put("f", defaultCreator);
        creators.put("g", defaultCreator);

        creators.put("ctl13", new CTL13MultilinearPairingCreator());
    }


    public Pairing initPairing(String parametersPath) {
        return initPairing(loadParameters(parametersPath), secureRandomCreator.newSecureRandom());
    }

    public Pairing initPairing(Parameters parameters) {
        return initPairing(parameters, secureRandomCreator.newSecureRandom());
    }

    public Pairing initPairing(String parametersPath, SecureRandom random) {
        return initPairing(loadParameters(parametersPath), random);
    }

    public Pairing initPairing(Parameters parameters, SecureRandom random) {
        if (parameters == null)
            throw new IllegalArgumentException("parameters cannot be null.");

        if (random == null)
            random = secureRandomCreator.newSecureRandom();

        Pairing pairing = null;
        if (reuseInstance) {
            pairing = instances.get(parameters);
            if (pairing != null)
                return pairing;
        }

        String type = parameters.getString("type");
        PairingCreator creator = creators.get(type);
        if (creator == null)
            throw new IllegalArgumentException("Type not supported. Type = " + type);

        pairing = creator.create(type, random, parameters);
        if (pairing == null)
            throw new IllegalArgumentException("Cannot create pairing instance. Type = " + type);

        if (immutable)
            pairing = new ImmutableParing(pairing);

        if (reuseInstance)
            instances.put(parameters, pairing);

        return pairing;
    }


    public Parameters loadParameters(String path) {
        PropertiesParameters curveParams = new PropertiesParameters();
        curveParams.load(path);

        return curveParams;
    }


    public boolean isPBCAvailable() {
        return pbcAvailable;
    }

    public boolean isUsePBCWhenPossible() {
        return usePBCWhenPossible;
    }

    public void setUsePBCWhenPossible(boolean usePBCWhenPossible) {
        this.usePBCWhenPossible = usePBCWhenPossible;
    }

    public boolean isReuseInstance() {
        return reuseInstance;
    }

    public void setReuseInstance(boolean reuseInstance) {
        this.reuseInstance = reuseInstance;
    }

    public boolean isImmutable() {
        return immutable;
    }

    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }


    public void addPairingCreator(String type, PairingCreator creator) {
        creators.put(type, creator);
    }


    public static interface PairingCreator {

        Pairing create(String type, SecureRandom random, Parameters parameters);

    }

    public static class CTL13MultilinearPairingCreator implements PairingCreator {

        private Method getPairingMethod;
        private Throwable throwable;

        public CTL13MultilinearPairingCreator() {
            try {
                Class factoryClass = Class.forName("org.arcanum.pairing.mm.clt13.pairing.CTL13MMPairingFactory");
                getPairingMethod = factoryClass.getMethod("getPairing", SecureRandom.class, Parameters.class);
            } catch (Exception e) {
                throwable = e;
            }

        }

        public Pairing create(String type, SecureRandom random, Parameters parameters) {
            try {
                return (Pairing) getPairingMethod.invoke(null, random, parameters);
            } catch (Exception e) {
                // Ignore
                e.printStackTrace();
            }
            return null;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    public class EllipticCurvesPairingCreator implements PairingCreator {

        private Method getPairingMethod;
        private Throwable pbcPairingFailure;

        public EllipticCurvesPairingCreator() {
            // Try to load arcanum-pbc factory
            try {
                Class pbcPairingFactoryClass = Class.forName("org.arcanum.pairing.pbc.PBCPairingFactory");
                Method isPBCAvailable = pbcPairingFactoryClass.getMethod("isPBCAvailable", null);

                pbcAvailable = ((Boolean) isPBCAvailable.invoke(null));
                if (pbcAvailable)
                    getPairingMethod = pbcPairingFactoryClass.getMethod("getPairing", Parameters.class);
            } catch (Exception e) {
                pbcAvailable = false;
                pbcPairingFailure = e;
            }

        }

        public Pairing create(String type, SecureRandom random, Parameters parameters) {
            Pairing pairing = null;

            // Handle bilinear maps parameters
            if (usePBCWhenPossible && pbcAvailable)
                pairing = getPBCPairing(parameters);

            if (pairing == null) {
                if ("a".equalsIgnoreCase(type))
                    pairing = new TypeAPairing(random, parameters);
                else if ("a1".equalsIgnoreCase(type))
                    pairing = new TypeA1Pairing(random, parameters);
                else if ("d".equalsIgnoreCase(type))
                    pairing = new TypeDPairing(random, parameters);
                else if ("e".equalsIgnoreCase(type))
                    pairing = new TypeEPairing(random, parameters);
                else if ("f".equalsIgnoreCase(type))
                    return new TypeFPairing(random, parameters);
                else if ("g".equalsIgnoreCase(type))
                    return new TypeGPairing(random, parameters);
                else
                    throw new IllegalArgumentException("Type not supported. Type = " + type);
            }

            return pairing;
        }

        public Pairing getPBCPairing(Parameters parameters) {
            try {
                return (Pairing) getPairingMethod.invoke(null, parameters);
            } catch (Exception e) {
                // Ignore
                e.printStackTrace();
            }
            return null;
        }

        public Throwable getPbcPairingFailure() {
            return pbcPairingFailure;
        }
    }


    public static interface SecureRandomCreator {

        SecureRandom newSecureRandom();

    }

    public static class DefaultSecureRandomCreator implements SecureRandomCreator {

        public SecureRandom newSecureRandom() {
            return new SecureRandom();
        }
    }

}
