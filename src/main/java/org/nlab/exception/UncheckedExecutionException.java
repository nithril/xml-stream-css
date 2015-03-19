package org.nlab.exception;



/**
 * From Guava
 * <p>
 * Unchecked variant of {@link java.util.concurrent.ExecutionException}. As with
 * {@code ExecutionException}, the exception's {@linkplain #getCause() cause}
 * comes from a failed task, possibly run in another thread.
 * <p>
 * <p>{@code UncheckedExecutionException} is intended as an alternative to
 * {@code ExecutionException} when the exception thrown by a task is an
 * unchecked exception. However, it may also wrap a checked exception in some
 * cases.
 * <p>
 * <p>When wrapping an {@code Error} from another thread, prefer
 * ExecutionError. When wrapping a checked exception, prefer {@code
 * ExecutionException}.
 *
 * @author Charles Fry
 * @since 10.0
 */
public class UncheckedExecutionException extends RuntimeException {
    /**
     * Creates a new instance with {@code null} as its detail message.
     */
    protected UncheckedExecutionException() {
    }

    /**
     * Creates a new instance with the given detail message.
     */
    protected UncheckedExecutionException(String message) {
        super(message);
    }

    /**
     * Creates a new instance with the given detail message and cause.
     */
    public UncheckedExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance with the given cause.
     */
    public UncheckedExecutionException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 0;
}