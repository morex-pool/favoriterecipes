package com.morteza.assignment.favoriterecipes.util;

import com.morteza.assignment.favoriterecipes.enums.Comparison;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    private Utils() {

    }

    public static Map<Comparison, List<Long>> comparisonNumbers(List<Comparison> comparisons, List<Long> list1, List<Long> list2) {

        if (comparisons.isEmpty())
            return null;

        if (list1 == null) list1 = Collections.emptyList();
        if (list2 == null) list2 = Collections.emptyList();

        // Variable used in lambda expression should be final
        List<Long> finalList1 = list1;
        List<Long> finalList2 = list2;

        Map<Comparison, List<Long>> result = new HashMap<>();

        // Find the difference between list1 and list2
        if (comparisons.contains(Comparison.DIFFERENTIATE)) {
            Set<Long> diff = Stream.concat(list1.stream(), list2.stream())
                    .filter(e -> !(finalList1.contains(e) && finalList2.contains(e)))
                    .collect(Collectors.toSet());
            if (!diff.isEmpty()) {
                result.put(Comparison.DIFFERENTIATE, new ArrayList<>(diff));
            }
        }

        // Find the common elements between list1 and list2
        if (comparisons.contains(Comparison.COMMON)) {
            Set<Long> common = list1.stream()
                    .filter(list2::contains)
                    .collect(Collectors.toSet());
            if (!common.isEmpty()) {
                result.put(Comparison.COMMON, new ArrayList<>(common));
            }
        }


        // Find elements in list1 that are not in list2
        if (comparisons.contains(Comparison.EXIST_JUST_IN_LIST1)) {
            Set<Long> onlyInList1 = list1.stream()
                    .filter(e -> !finalList2.contains(e))
                    .collect(Collectors.toSet());
            if (!onlyInList1.isEmpty()) {
                result.put(Comparison.EXIST_JUST_IN_LIST1, new ArrayList<>(onlyInList1));
            }
        }

        // Find elements in list2 that are not in list1
        if (comparisons.contains(Comparison.EXIST_JUST_IN_LIST1)) {
            Set<Long> onlyInList2 = list2.stream()
                    .filter(e -> !finalList1.contains(e))
                    .collect(Collectors.toSet());
            if (!onlyInList2.isEmpty()) {
                result.put(Comparison.EXIST_JUST_IN_LIST2, new ArrayList<>(onlyInList2));
            }
        }

        return result;
    }
}
