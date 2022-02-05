package utils;

/**
 * Helper interface representing method with 2 arguments and output value. Used for lambda expressions.
 * @param <TInput1> Type of the 1st argument
 * @param <TInput2> Type of the 2nd argument
 * @param <TOutput> Type of the output
 */
public interface Func2Args<TInput1, TInput2, TOutput> {
    TOutput apply(TInput1 arg1, TInput2 arg2);
}
