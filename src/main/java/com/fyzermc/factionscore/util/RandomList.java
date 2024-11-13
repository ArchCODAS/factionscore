package com.fyzermc.factionscore.util;

import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomList<E> extends ArrayList<RandomList.RandomCollectionObject<E>> {

    private final Random random;

    public RandomList() {
        this(new Random());
    }

    public RandomList(Random random) {
        this.random = random;
    }

    public Random getRandom() {
        return random;
    }

    public boolean add(E e, double weight) {
        return this.add(new RandomCollectionObject<>(e, weight));
    }

    @Override
    public boolean remove(Object o) {
        return super.removeIf(rco -> rco.getObject().equals(o));
    }

    public E raffle() {
        return raffle(this);
    }

    private E raffle(RandomList<E> list) {
        NavigableMap<Double, RandomCollectionObject<E>> auxMap = new TreeMap<>();

        list.forEach((rco) -> {
            double auxWeight = auxMap.isEmpty() ? 0.0 : auxMap.lastKey();
            auxWeight += rco.getWeight();

            auxMap.put(auxWeight, rco);
        });

        double totalWeight = list.getRandom().nextDouble() * auxMap.lastKey();

        return auxMap.ceilingEntry(totalWeight).getValue().getObject();
    }

    public static class RandomCollectionObject<T> {

        private final T object;
        private final double weight;

        public RandomCollectionObject(T object, double weight) {
            this.object = object;
            this.weight = weight;
        }

        public T getObject() {
            return this.object;
        }

        public double getWeight() {
            return this.weight;
        }
    }
}