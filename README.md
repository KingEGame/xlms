# xlms

application.properites: 
надо изменить логин и пароль базы данных PostgreSQL

Пути как работает программа:
/ - Главный пкть где видим ссылки :
        greeting = проверка работы приложения
        list objects - чтобы увидеть все записи из базы данных
        parse - пройдя по пути мы добавим данные из файла resorces/text.txt в нагу базу
        Calculation (/Calc/type - ) - документ выводящий нам месячный отчет по парку с вычисленными растояниями, потраченого топлива, Факт. и норм. перевезенная массаи, Типы и количесва работавших эесковаторов и самосвалов:
          По следуюзим ссылкам: 
           ссылка Optimize для высичления затрат по месяцу
            По каждому эксковатору (/calc/perdayByEx/{день}) - спискок по каждому эксковатору с связанным с ним самосвалами:
                Ссылка optimze нужна для вывода за этот день вычисления затрат
            По каждому самосвалу (calc/perdayByTruck/{день}) - список работавших в тот день самосвалов с вычислеными значениями
            