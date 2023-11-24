# Genetic-Algorithm

CLI program written in Java which tries to evolve the best mapping between students and lecturers (supervisors for projects) based on student preferences and lecturer capacities.
(First few branches involve evolving a solution to simpler problems with the chromosome being a binary string)

Description of representation:
The chromosomes are represented as an one dimensional array of integers with the length
equal to the number of students – 1.
Each cell in this array can be assigned a value in range 1 to the number of supervisors.
These values are the ID’s of supervisors.
The index in the chromosome represents the student. (Indexes are shifted by -1 to satisfy the
array representation => Student 1 is found under the call array[0])
If we have a list of 10 students and 5 supervisors we can represent a randomly generated
chromosome as: [1][3][2][4][5][2][1][3][5][3]
This would correspond to the below mapping where supervisor under Supervisor ID supervises
all students under Student ID on the same row.
<br>
<table align="center">
  <tr>
    <td>
    Supervisor ID
    </td>
    <td>
      Student ID
    </td>
  </tr>
  <tr>
    <td>
      1
    </td>
    <td>
      1, 7
    </td>
  </tr>
  <tr>
    <td>
      2
    </td>
    <td>
    3, 6
    </td>
  </tr>
  <tr>
    <td>
      3
    </td>
    <td>
      2, 8, 10
    </td>
  </tr>
  <tr>
    <td>
      4
    </td>
    <td>
      4
    </td>
  </tr>
  <tr>
    <td>
      5
    </td>
    <td>
      5, 9
    </td>
  </tr>
</table>

