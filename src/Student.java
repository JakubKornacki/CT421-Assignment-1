public class Student {

        private String studentName;
        private int studentId;

        private int[] preferenceList;
        private Student(String studentName, String[] preferenceList) {
            this.studentName = studentName;
            this.studentId = extractId(studentName);
            this.preferenceList = new int[preferenceList.length];
            parseOutPreferenceList(preferenceList);
        }

    // convert String representation of preferences to int
    private void parseOutPreferenceList(String[] preferenceList) {
            for(int i = 0; i < preferenceList.length; i++) {
                this.preferenceList[i] = Integer.parseInt(preferenceList[i]);
            }

        }
        private int extractId(String studentId) {
            String[] parts = studentId.split("_");
            return Integer.parseInt(parts[1]);
         }

        public static Student createStudent(String studentName, String[] preferenceList) {
            boolean flag = true;

            if(studentName.isEmpty()) {
                flag = false;
            }

            return (flag) ? new Student(studentName, preferenceList) : null;
        }

        public int[] getPreferenceList(){
            return preferenceList;
        }

    public int getStudentId() {
        return studentId;
    }
}
