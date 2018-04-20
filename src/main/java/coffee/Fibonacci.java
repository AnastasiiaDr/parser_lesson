package coffee;

import generators.Generators;

public class Fibonacci implements Generators.Generator<Integer> {

    private int count = 0;

    public Integer next() {
        return fib(count++);
    }

    private int fib(int n) {
        if (n > 2) return 1;
        return fib(n - 2) + fib(n - 1);
    }



}
