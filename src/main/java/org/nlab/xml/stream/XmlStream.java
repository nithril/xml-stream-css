package org.nlab.xml.stream;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.nlab.xml.stream.context.StreamContext;
import org.nlab.xml.stream.predicate.Predicates;
import org.nlab.xml.stream.predicate.XmlPredicate;
import org.nlab.xml.stream.reader.XmlMatcherStreamReader;

import static org.nlab.xml.stream.predicate.Predicates.css;

/**
 * Created by nlabrot on 14/12/15.
 */
public class XmlStream implements Stream<StreamContext> {

	private final Stream<StreamContext> delegate;
	private final XmlMatcherStreamReader xmlMatcherStreamReader;

	public XmlStream(Stream<StreamContext> delegate, XmlMatcherStreamReader xmlMatcherStreamReader) {
		this.delegate = delegate;
		this.xmlMatcherStreamReader = xmlMatcherStreamReader;
	}

	public XmlStream css(String css){
		return filter(Predicates.css(css));
	}

	@Override
	public XmlStream filter(Predicate<? super StreamContext> predicate) {
		if (predicate instanceof XmlPredicate) {
			if (((XmlPredicate) predicate).requireSibbling()) {
				xmlMatcherStreamReader.requireSibbling();
			}
		}
		return new XmlStream(delegate.filter(predicate), xmlMatcherStreamReader);
	}

	@Override
	public <R> Stream<R> map(Function<? super StreamContext, ? extends R> mapper) {
		return delegate.map(mapper);
	}

	@Override
	public IntStream mapToInt(ToIntFunction<? super StreamContext> mapper) {
		return delegate.mapToInt(mapper);
	}

	@Override
	public LongStream mapToLong(ToLongFunction<? super StreamContext> mapper) {
		return delegate.mapToLong(mapper);
	}

	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super StreamContext> mapper) {
		return delegate.mapToDouble(mapper);
	}

	@Override
	public <R> Stream<R> flatMap(Function<? super StreamContext, ? extends Stream<? extends R>> mapper) {
		return delegate.flatMap(mapper);
	}

	@Override
	public IntStream flatMapToInt(Function<? super StreamContext, ? extends IntStream> mapper) {
		return delegate.flatMapToInt(mapper);
	}

	@Override
	public LongStream flatMapToLong(Function<? super StreamContext, ? extends LongStream> mapper) {
		return delegate.flatMapToLong(mapper);
	}

	@Override
	public DoubleStream flatMapToDouble(Function<? super StreamContext, ? extends DoubleStream> mapper) {
		return delegate.flatMapToDouble(mapper);
	}

	@Override
	public Stream<StreamContext> distinct() {
		return delegate.distinct();
	}

	@Override
	public Stream<StreamContext> sorted() {
		return delegate.sorted();
	}

	@Override
	public Stream<StreamContext> sorted(Comparator<? super StreamContext> comparator) {
		return delegate.sorted(comparator);
	}

	@Override
	public Stream<StreamContext> peek(Consumer<? super StreamContext> action) {
		return delegate.peek(action);
	}

	@Override
	public Stream<StreamContext> limit(long maxSize) {
		return delegate.limit(maxSize);
	}

	@Override
	public Stream<StreamContext> skip(long n) {
		return delegate.skip(n);
	}

	@Override
	public void forEach(Consumer<? super StreamContext> action) {
		delegate.forEach(action);
	}

	@Override
	public void forEachOrdered(Consumer<? super StreamContext> action) {
		delegate.forEachOrdered(action);
	}

	@Override
	public Object[] toArray() {
		return delegate.toArray();
	}

	@Override
	public <A> A[] toArray(IntFunction<A[]> generator) {
		return delegate.toArray(generator);
	}

	@Override
	public StreamContext reduce(StreamContext identity, BinaryOperator<StreamContext> accumulator) {
		return delegate.reduce(identity, accumulator);
	}

	@Override
	public Optional<StreamContext> reduce(BinaryOperator<StreamContext> accumulator) {
		return delegate.reduce(accumulator);
	}

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super StreamContext, U> accumulator, BinaryOperator<U> combiner) {
		return delegate.reduce(identity, accumulator, combiner);
	}

	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super StreamContext> accumulator, BiConsumer<R, R> combiner) {
		return delegate.collect(supplier, accumulator, combiner);
	}

	@Override
	public <R, A> R collect(Collector<? super StreamContext, A, R> collector) {
		return delegate.collect(collector);
	}

	@Override
	public Optional<StreamContext> min(Comparator<? super StreamContext> comparator) {
		return delegate.min(comparator);
	}

	@Override
	public Optional<StreamContext> max(Comparator<? super StreamContext> comparator) {
		return delegate.max(comparator);
	}

	@Override
	public long count() {
		return delegate.count();
	}

	@Override
	public boolean anyMatch(Predicate<? super StreamContext> predicate) {
		return delegate.anyMatch(predicate);
	}

	@Override
	public boolean allMatch(Predicate<? super StreamContext> predicate) {
		return delegate.allMatch(predicate);
	}

	@Override
	public boolean noneMatch(Predicate<? super StreamContext> predicate) {
		return delegate.noneMatch(predicate);
	}

	@Override
	public Optional<StreamContext> findFirst() {
		return delegate.findFirst();
	}

	@Override
	public Optional<StreamContext> findAny() {
		return delegate.findAny();
	}

	public static <T> Builder<T> builder() {
		return Stream.builder();
	}

	public static <T> Stream<T> empty() {
		return Stream.empty();
	}

	public static <T> Stream<T> of(T t) {
		return Stream.of(t);
	}

	@SafeVarargs
	public static <T> Stream<T> of(T... values) {
		return Stream.of(values);
	}

	public static <T> Stream<T> iterate(T seed, UnaryOperator<T> f) {
		return Stream.iterate(seed, f);
	}

	public static <T> Stream<T> generate(Supplier<T> s) {
		return Stream.generate(s);
	}

	public static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b) {
		return Stream.concat(a, b);
	}

	@Override
	public Iterator<StreamContext> iterator() {
		return delegate.iterator();
	}

	@Override
	public Spliterator<StreamContext> spliterator() {
		return delegate.spliterator();
	}

	@Override
	public boolean isParallel() {
		return delegate.isParallel();
	}

	@Override
	public Stream<StreamContext> sequential() {
		return delegate.sequential();
	}

	@Override
	public Stream<StreamContext> parallel() {
		return delegate.parallel();
	}

	@Override
	public Stream<StreamContext> unordered() {
		return delegate.unordered();
	}

	@Override
	public Stream<StreamContext> onClose(Runnable closeHandler) {
		return delegate.onClose(closeHandler);
	}

	@Override
	public void close() {
		delegate.close();
	}

	public XmlMatcherStreamReader getXmlMatcherStreamReader() {
		return xmlMatcherStreamReader;
	}
}
