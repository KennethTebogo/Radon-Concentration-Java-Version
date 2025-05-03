public class RadonCalculator {

    // Constants
    private static final double SYSTEM_ERROR_PERCENT = 5.0;
    private static final double VOLTAGE_READING_ERROR = 1.0; // ±1 V for both I and F
    private static final double BACKGROUND_GAMMA = 32.0;     // Bq/m³
    private static final double BACKGROUND_ERROR = 6.0;      // Bq/m³

    public static double computeCalibrationFactor(double I, double F) {
        return 0.0459 + 1.5519e-5 * (I + F);
    }

    public static double computeRadonConcentration(double I, double F, double T, double Cf, double BG) {
        return ((I - F) / (Cf * T)) - BG;
    }

    public static double computeVoltageErrorPercent(double I, double F) {
        double deltaV = 2 * VOLTAGE_READING_ERROR; // ±1 V each
        return (deltaV / Math.abs(I - F)) * 100.0;
    }

    public static double computeBackgroundErrorPercent(double CRn, double BG) {
        return (BACKGROUND_ERROR / (CRn + BG)) * 100.0;
    }

    public static double computeTotalError(double E1, double E2, double E3) {
        return Math.sqrt(E1 * E1 + E2 * E2 + E3 * E3);
    }

    public static void main(String[] args) {
        double I = 708.0;  // Initial voltage
        double F = 640.0;  // Final voltage
        double T = 7.0;    // Exposure time in days

        double deltaV = I - F;
        double Cf = computeCalibrationFactor(I, F);
        double CRn = computeRadonConcentration(I, F, T, Cf, BACKGROUND_GAMMA);

        double E1 = SYSTEM_ERROR_PERCENT;
        double E2 = computeVoltageErrorPercent(I, F);
        double E3 = computeBackgroundErrorPercent(CRn, BACKGROUND_GAMMA);
        double totalError = computeTotalError(E1, E2, E3);

        System.out.printf("Initial Voltage (V): %.2f%n", I);
        System.out.printf("Final Voltage (V): %.2f%n", F);
        System.out.printf("Exposure Time (days): %.2f%n", T);
        System.out.printf("Voltage Drop (V): %.2f%n", deltaV);
        System.out.printf("Calibration Factor (Cf): %.5f%n", Cf);
        System.out.printf("Radon Concentration (Bq/m³): %.2f%n", CRn);
        System.out.println("\n--- Error Estimates ---");
        System.out.printf("System Component Error (E1): %.2f%%%n", E1);
        System.out.printf("Voltage Reading Error (E2): %.2f%%%n", E2);
        System.out.printf("Gamma Background Error (E3): %.2f%%%n", E3);
        System.out.printf("Total Estimated Error: %.2f%%%n", totalError);
    }
}

