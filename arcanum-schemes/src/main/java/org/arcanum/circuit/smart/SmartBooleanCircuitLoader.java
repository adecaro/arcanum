package org.arcanum.circuit.smart;

import org.arcanum.circuit.BooleanCircuit;
import org.arcanum.circuit.Gate;
import org.arcanum.util.io.IOUtils;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.StringTokenizer;

import static org.arcanum.circuit.BooleanCircuit.BooleanCircuitGate;
import static org.arcanum.circuit.Gate.Type.INPUT;

/**
 * @author Angelo De Caro (arcanumlib@gmail.com)
 */
public class SmartBooleanCircuitLoader {

    public BooleanCircuit load(String path) {
        try {
            InputStream is = IOUtils.openInputStream(path);
            DataInput di = new DataInputStream(is);

            // A line defining the number of gates and then the number of wires in the circuit.
            String header = di.readLine();
            StringTokenizer st = new StringTokenizer(header, " ");
            int nGates = Integer.valueOf(st.nextToken());
            int nWires = Integer.valueOf(st.nextToken());

            System.out.println("nGates = " + nGates);
            System.out.println("nWires = " + nWires);

            // Then two numbers defining the number n1 and n2 of wires
            // in the inputs to the function given by the circuit.
            // We assume the function has at most two inputs; since most of our examples do.
            // If the function has only one input then the second inputs size is set to zero.
            // Then on the same line comes the number of wires in the output n3.
            header = di.readLine();

            st = new StringTokenizer(header, " ");
            int n1 = Integer.valueOf(st.nextToken());
            int n2 = Integer.valueOf(st.nextToken());
            int n3 = Integer.valueOf(st.nextToken());

            if (n3 != 1)
                throw new IllegalArgumentException("Only one output accepted");

            System.out.println("n1 = " + n1);
            System.out.println("n2 = " + n2);
            System.out.println("n3 = " + n3);

            BooleanCircuit circuit = new BooleanCircuit(n1 + n2, nGates, nWires);
            for (int i = 0; i < circuit.getNumInputs(); i++) {
                circuit.addGate(new BooleanCircuitGate(INPUT, i));
            }

            // After this the gates are listed in the format:
            //            Number input wires
            //            Number output wires
            //            List of input wires
            //            List of output wires
            //            Gate operation (XOR, AND or INV).
            //            So for example
            //                2 1 3 4 5 XOR
            //
            //            corresponds to
            //              w5=XOR(w3,w4).
            while (true) {
                String line = di.readLine();

                if (line == null)
                    break;

                line = line.trim();

                if (line.isEmpty())
                    continue;

                System.out.println("line = " + line);

                st = new StringTokenizer(line, " ");
                int nIns = Integer.valueOf(st.nextToken());
                int nOuts = Integer.valueOf(st.nextToken());

                int[] ins = new int[nIns];
                for (int i = 0; i < nIns; i++)
                    ins[i] = Integer.valueOf(st.nextToken());

                int[] outs = new int[nOuts];
                for (int i = 0; i < nOuts; i++)
                    outs[i] = Integer.valueOf(st.nextToken());

                Gate.Type type = Gate.Type.valueOf(st.nextToken());

                circuit.addGate(new BooleanCircuitGate(type, ins, outs));
            }
            circuit.computeDepths();

            return circuit;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
