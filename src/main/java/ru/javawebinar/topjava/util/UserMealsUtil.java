package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> list = getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();//делать по дням сумму по калориям
//        .toLocalTime();//для того чтобы фильтровать по времени

    }

    public static List<UserMealWithExceed>  getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        List<UserMealWithExceed> getList = new ArrayList<>();

        //Механизм расчета перерасхода калорий (данное исполнение учитывает, что у товарища всегда есть завтрак, обед и ужин)
        boolean[] exceedValues = new boolean[mealList.size()];
        int key = 0;
        int sum = 0;
        for (UserMeal user : mealList) {
            //условие что все дни с обедом, днем и ужином
            if (mealList.size() % 3 == 0) {
                if ((key + 1) % 3 != 0) {
                    sum += user.getCalories();
                    key++;
                } else {
                    sum += user.getCalories();
                    if (sum <= caloriesPerDay) {
                        exceedValues[key] = false;
                        exceedValues[key - 1] = false;
                        exceedValues[key - 2] = false;
                    } else {
                        exceedValues[key] = true;
                        exceedValues[key - 1] = true;
                        exceedValues[key - 2] = true;
                    }
                    sum = 0;
                    key++;
                }
            }
        }
        //Заполнение List<UserMealWithExceed> getList с уже просчитанным показателем перерасхода калорий
        int keyExceedValues = 0;
        for (UserMeal user : mealList) {
            UserMealWithExceed update = new UserMealWithExceed(user.getDateTime(),user.getDescription(),user.getCalories(),exceedValues[keyExceedValues]);
            keyExceedValues++;
            //Заполняем с учетом указанного времени выборки
            boolean keyUpdate = TimeUtil.isBetween(user.getDateTime().toLocalTime(),startTime,endTime);
            if (keyUpdate) getList.add(update);
        }
        return getList;
    }
}
