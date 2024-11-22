public class Test {
        public static void main(String[] args) {
            try {
                Class.forName("org.postgresql.Driver");
                System.out.println("PostgreSQL Driver loaded successfully!");
            } catch (ClassNotFoundException e) {
                System.err.println("Driver not found: " + e.getMessage());
            }
        }
}
