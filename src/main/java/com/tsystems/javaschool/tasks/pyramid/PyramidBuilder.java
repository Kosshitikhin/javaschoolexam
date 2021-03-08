import com.tsystems.javaschool.tasks.pyramid.CannotBuildPyramidException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        int[][] pyramid;

        if (inputNumbers.contains(null) || inputNumbers.size() <= 1 || inputNumbers.size() > Integer.MAX_VALUE - 8) {
            throw new CannotBuildPyramidException();
        } else {
            List<Integer> inputSorted = inputNumbers.stream().sorted().collect(Collectors.toList());
            int size = inputNumbers.size(), rows = 1, columns = 1, counter = 0;

            while (counter < size) {
                counter = counter + rows;
                rows++;
                columns = columns +2;
            }

            rows = rows - 1;            //истинное значение строк
            columns = columns - 2;      //Истинное значение столбцов

            if (size == counter) {
                /**             ЗАПОЛНЯЕМ МАТРИЦУ НУЛЯМИ          **/
                pyramid = new int[rows][columns];
                for (int[] row : pyramid) {
                    Arrays.fill(row, 0);
                }

                /**                   СТРОИМ ПИРАМИДУ                  **/
                int center = columns / 2;    // центральная точка матрицы
                int count = 1;      //кол-во символов в строке
                int inputSortedIdx = 0;

                for (int i = 0, offset = 0; i < rows; i++, offset++, count++) {
                    int start = center - offset;
                    for (int j = 0; j < count * 2; j += 2, inputSortedIdx++) {
                        pyramid[i][start + j] = inputSorted.get(inputSortedIdx);
                    }
                }
            } else {
                throw new CannotBuildPyramidException();
            }
        }

        return pyramid;
    }

}
