package TeamiumPremium;

import org.neuroph.core.transfer.TransferFunction;

public class LeakyRectified extends TransferFunction {
    @Override
    public double getOutput(double net) {
        if (net > Double.MIN_VALUE) {
            return net;
        }
        return 0.01 * net;
    }

    public double getDerivative(double net) {
        if (net > Double.MIN_VALUE)
            return 1;
        return 0.01;
    }
}