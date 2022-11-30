import java.util.LinkedList;
import java.util.List;

public class PrisonerRiddle {

    public static class Guesser {
        public int id;
        public int[] memory;
        private int memoryDepth;
        public Guesser(int id, int memorySize) {
            this.id = id;
            this.memory = new int[memorySize];
            this.memoryDepth = 0;
        }

        public void addMemory(int id) {
            this.memory[this.memoryDepth] = id;
            this.memoryDepth++;
        }

        public boolean duplicateCheck(int id) {
            for(int i = 0; i < this.memoryDepth; i++) {
                if (this.memory[i] == id) {
                    return true;
                }
            }
            return false;
        }

        public void clearMemory() {
            this.memoryDepth = 0;
            this.memory = new int[this.memory.length];
        }
    }

    public static class Box {
        public int id;
        public int value;
        public Box(int id, int value) {
            this.id = id;
            this.value = value;
        }
    }

    public static List<Box> boxes = new LinkedList<>();
    public static List<Guesser> guessers = new LinkedList<>();

    public static int guesserAndBoxCount = 100;
    public static int shuffleIterations = 10;
    public static int guessCount = 50;

    public static void main(String[] args) {
        for (int x = 0; x < guesserAndBoxCount; x++) {
            boxes.add(new Box(x, x));
            guessers.add(new Guesser(x, guessCount));
        }

        long start = System.currentTimeMillis();
        for (int x = 0; x < shuffleIterations; x++) shuffle();
        System.out.println("Shuffle took " + (System.currentTimeMillis() - start) + "ms for " + shuffleIterations + " iterations");


        start = System.currentTimeMillis();
        int success = 0;
        for (int x = 0; x < 100; x++) {
            if (findBox(guessers.get(x), null, 0)) {
                success++;
            }
        }
        System.out.println("FindBox took " + (System.currentTimeMillis() - start) + "ms");

        System.out.println("Found Box = " + success);
        System.out.println("Lost Box = " + (100 - success));
        System.out.println("Total Boxes = " + boxes.size());
        System.out.println("Total Guessers = " + guessers.size());
    }

    private static void shuffle() {
        for (int x = 0; x < 100; x++) {
            int index = (int) (Math.random() * 100);
            Box temp = boxes.get(x);
            boxes.set(x, boxes.get(index));
            boxes.set(index, temp);
        }
    }

    private static boolean findBox(Guesser guesser, Box currentBox, int depth) {
        if (depth == guessCount) {
            return false;
        }

        if (currentBox == null) {
            currentBox = boxes.get(guesser.id);
            guesser.addMemory(currentBox.value);
        }

        if (currentBox.value == guesser.id) {
            return true;
        }

        if (guesser.duplicateCheck(currentBox.value)) {
            currentBox = boxes.get((int) (Math.random() * 100));
            guesser.clearMemory();
            return findBox(guesser, currentBox, depth + 1);
        }

        currentBox = boxes.get(currentBox.id);
        guesser.addMemory(currentBox.value);
        return findBox(guesser, currentBox, depth + 1);
    }

}
