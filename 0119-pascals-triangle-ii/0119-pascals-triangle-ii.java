class Solution {
    public List<Integer> getRow(int rowIndex) {

        List<List<Integer>> triangle = new ArrayList();
        List<Integer> first_row = new ArrayList();

        if (0 > rowIndex)
            return first_row;

        first_row.add(1);

        if (0 == rowIndex)
            return first_row;

            
        triangle.add(first_row);

        for (int i = 1; i <= rowIndex; i++) {

            List<Integer> prev_row = triangle.get(i - 1);
            List<Integer> curr_row = new ArrayList();
            curr_row.add(1);
            for (int j = 1; j < i; j++) {
                curr_row.add(prev_row.get(j) + prev_row.get(j - 1));
            }

            curr_row.add(1);
            triangle.add(curr_row);

        }

        return triangle.get(rowIndex);

    }
}