package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

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

        //вытаскиваем даты
        List<LocalDate> date = new ArrayList<>();
        date.add(mealList.get(0).getDateTime().toLocalDate());
        for (int i = 1; i < mealList.size(); i++) {
            if (!(mealList.get(i).getDateTime().toLocalDate().equals(mealList.get(i-1).getDateTime().toLocalDate()))){
                date.add(mealList.get(i).getDateTime().toLocalDate());
            }
        }

        //Считаем количество калорий
        int[] call = new int[date.size()];
        int day = 0;
        int sum = 0;
        for (int i = 0; i < mealList.size(); i++) {
            if ((mealList.get(i).getDateTime().toLocalDate()).equals(date.get(day))){
                sum += mealList.get(i).getCalories();
                if ( i < mealList.size()-1) continue;
            }
            call[day] = sum;
            day++;
            sum = mealList.get(i).getCalories();
        }

        //карта ключ - дата, значение - количество сожранных калорий
        Map<LocalDate,Integer> calInDate = new HashMap<>();
        for (int i = 0; i < date.size(); i++) {
            calInDate.put(date.get(i),call[i]);
        }

        //Заполнение List<UserMealWithExceed> getList с уже просчитанным показателем перерасхода калорий
        for (UserMeal user : mealList) {
            boolean isEceed = false;
            LocalDate localDate = user.getDateTime().toLocalDate();
            int cal = calInDate.get(localDate);
            if (cal>caloriesPerDay) {
                isEceed = true;
            }
            LocalDate temp = user.getDateTime().toLocalDate();
            UserMealWithExceed update = new UserMealWithExceed(user.getDateTime(),user.getDescription(),user.getCalories(),isEceed);
            //Заполняем с учетом указанного времени выборки
            boolean keyUpdate = TimeUtil.isBetween(user.getDateTime().toLocalTime(),startTime,endTime);
            if (keyUpdate) getList.add(update);
        }
        return getList;
    }
}
