package org.haldokan.edge.interviewquest.amazon;

import com.google.inject.internal.cglib.core.$Block;

import java.util.HashMap;
import java.util.Map;

/**
 * My solution to an Amazon interview question: simultaneously check for blood relation on the father and mother side and
 * returned as soon as either evaluates to true
 *
 * The Question: 3.5-STAR
 * Given billions of Identity cards of the form :
 *
 * card : {
 *    "my_id" : "my id",
 *    "moms_id" : "mom id",
 *    "dad_id" : "dads id"
 * }
 * If one gives you two Person's id, how can you tell if these 2 persons are blood related.
 * So, write a function that is:
 *
 * def is_blood_related( person_id_1, person_id_2 )
 */
public class BloodRelationBasedOnIdCards {
    Map<String, String> paternalAncestors = Map.of("id1", "id2", "id2", "id3", "id3", "id4", "id5", "id6");
    Map<String, String> maternalAncestors = Map.of("id1", "idd2", "idd2", "id3", "id3", "idd4", "id5", "idd5");

    public static void main(String[] args) {
        BloodRelationBasedOnIdCards driver = new BloodRelationBasedOnIdCards();
        System.out.println(driver.bloodRelated(new IdCard("id1", "id2", "idd2"), new IdCard("id3", "id4", "idd4")));
        System.out.println(driver.bloodRelated(new IdCard("id1", "id2", "idd2"), new IdCard("id5", "id6", "idd5")));
    }

    boolean bloodRelated(IdCard card1, IdCard card2) {
        String male = card1.id;
        String female = card1.id;
        for (; ;) {
            String father = paternalAncestors.get(male);
            if (father != null) {
                if (father.equals(card2.father)) {
                    return true;
                }
                male = father;
            }

            String mother = maternalAncestors.get(female);
            if (mother != null) {
                if (mother.equals(card2.mother)) {
                    return true;
                }
                female = mother;
            }

            if (father == null && mother == null) {
                return false;
            }
        }
    }

    static class IdCard {
        String id;
        String father;
        String mother;

        public IdCard(String id, String father, String mother) {
            this.id = id;
            this.father = father;
            this.mother = mother;
        }
    }
}
