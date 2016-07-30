package org.haldokan.edge.interviewquest.amazon;


import com.google.common.collect.ImmutableSet;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * My solution to an Amazon interview question - originally thought it was a case of the Bridge pattern but it turned out
 * to be a less interesting question.
 * The Question: 3_STAR
 * <p>
 * A furniture can be made of material like metal, wood, etc. or any combination of them. Also there are different
 * furniture types chair, table, sofa, etc.
 * Wood furniture should be tested against suffocation, metal furniture is tested against fire, etc.
 * To make the question more interesting *I* will add the extra requirement that different kinds of furniture have
 * different types of furniture tests
 * <p>
 * Design the data structures.
 * <p>
 * Created by haytham.aldokanji on 7/29/16.
 */
public class FurnitureAndMaterialTesting {

    private static abstract class Furniture {
        private final Set<Material> materials;
        private final Set<FurnitureTest> tests;

        public Furniture(Set<Material> materials, Set<FurnitureTest> furnitureTests) {
            this.materials = ImmutableSet.copyOf(materials);

            tests = new ImmutableSet.Builder<FurnitureTest>()
                    .addAll(furnitureTests)
                    .addAll(this.materials.stream()
                            .flatMap(material -> material.getTests().stream())
                            .collect(Collectors.toSet()))
                    .build();
        }

        public Set<FurnitureTest> getTests() {
            return tests;
        }
    }

    private static class Table extends Furniture {
        public Table(Set<Material> materials, Set<FurnitureTest> furnitureTests) {
            super(materials, furnitureTests);
        }
    }

    private static class Chair extends Furniture {
        public Chair(Set<Material> materials, Set<FurnitureTest> furnitureTests) {
            super(materials, furnitureTests);
        }
    }

    private static class Sofa extends Furniture {
        public Sofa(Set<Material> materials, Set<FurnitureTest> furnitureTests) {
            super(materials, furnitureTests);
        }
    }

    private static abstract class Material {
        private final Set<FurnitureTest> tests;

        public Material(Set<FurnitureTest> tests) {
            this.tests = ImmutableSet.copyOf(tests);
        }

        public Set<FurnitureTest> getTests() {
            return tests;
        }
    }

    private static class Wood extends Material {
        public Wood(Set<FurnitureTest> tests) {
            super(tests);
        }
    }

    private static class Metal extends Material {
        public Metal(Set<FurnitureTest> tests) {
            super(tests);
        }
    }

    private static class Glass extends Material {
        public Glass(Set<FurnitureTest> tests) {
            super(tests);
        }
    }

    private static abstract class FurnitureTest {

    }

    private static class Stiffness extends FurnitureTest {

    }

    private static class Fire extends FurnitureTest {

    }

    private static class Suffocation extends FurnitureTest {

    }

    private static class Balance extends FurnitureTest {

    }

    // this is Spring context in a real app where furnitureTests are singleton beans
    private static class TestFactory {
        public static FurnitureTest getStiffnessTest() {
            return new Stiffness();
        }

        public static FurnitureTest getFireTest() {
            return new Fire();
        }

        public static FurnitureTest getBalanceTest() {
            return new Balance();
        }
    }
}
