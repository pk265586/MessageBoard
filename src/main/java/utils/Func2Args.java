package utils;

public interface Func2Args<TInput1, TInput2, TOutput> {
    TOutput apply(TInput1 arg1, TInput2 arg2);
}
