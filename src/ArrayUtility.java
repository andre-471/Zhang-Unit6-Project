public class ArrayUtility {
    private ArrayUtility() {}

     public static boolean ifValueInArray(Object value, Object[] array) {
        for (Object arrayValue : array) {
            if (value.equals(arrayValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean ifValueInArray(int value, int[] array) {
        for (int arrayValue : array) {
            if (value == arrayValue) {
                return true;
            }
        }

        return false;
    }


    public static int getValueCount(int value, int[] array) {
        int count = 0;

        for (int arrayValue : array) {
            if (value == arrayValue) {
                count++;
            }
        }

        return count;
    }

    public static boolean allEqual(int[] array) {
        if (array.length == 0) { return false; }

        int toCompare = array[0];

        for (int value : array) {
            if (toCompare != value) {
                return false;
            }
        }

        return true;
    }

    public static boolean ifEqual(int[] array1, int[] array2) {
        if (array1.length != array2.length) { return false; }

        // this is horribly inefficient
        for (int value : array1) {
            if (!ifValueInArray(value, array2)) {
                return false;
            }
        }

        for (int value : array2) {
            if (!ifValueInArray(value, array1)) {
                return false;
            }
        }

        return true;
    }

    public static boolean ifDuplicateValues(int[] array) {
        for (int value : array){
            int count = getValueCount(value, array);
            if (count > 1 && count < array.length) {
                return true;
            }
        }

        return false;
    }

    public static int getRarestValue(int[] array) {
        int rarestValue = 0;
        int rarestCount = Integer.MAX_VALUE;
        for (int value : array) {
            int count = getValueCount(value, array);
            if (count < rarestCount) {
                rarestValue = value;
                rarestCount = count;
            }
        }

        return rarestValue;
    }
}
