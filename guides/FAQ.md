Как работать с enum?

- В сущности: над enum-полем ставим <code>@Enumerated(EnumType.STRING)</code>. Сам enum делаем вложенным классом сущности
- В БД: enum не будет отдельной таблицей, а будет частью основной таблицы, полем типа <code>VARCHAR</code>
- В ДТО: просто поле-enum

Как работать с наследованием сущностей?

- Есть несколько способов работать с наследованием сущностей, но мы воспользуемся самым простым - делаем свою таблицу для каждой сущности в иерархии (Table Per Class Inheritance).
Пример: Есть абстрактный класс-сущность Animal, у него есть поля String name, int age. Есть два не-абстрактных наследника Dog и Cat. Ставим @Entity над всеми тремя классами, а в БД создаем две таблицы: dog и cat. У них будут поля BIGSERIAL id, VARCHAR(255) name, INT age. Вы могли заметить, что на уровне БД мы имеем дублирование колонок, его можно избежать при других подходах работы с наследованием сущностей (Joined Table Inheritance и Single Table Inheritance), но эти подходы чуть сложнее в реализации

Как работать с изображениями?
 
- Делаем эндпоинты для работы с изображениями отдельно от основных CRUD-эндпоинтов. Такой подход несложно реализовать в коде, а клиент может запросить изображения только тогда, когда они ему действительно нужны. Например, клиент делает GET запрос на получение 100 сущностей, но он не хочет вместе с ними получать их изобажения, поскольку это может занять сильно больше времени, чем получение текстовой информации, при этом пользователю надо показать хотя бы какие-то данные, а не заставлять его ждать, когда загрузятся все изображения. Пример можно посмотреть в репозитории проекта в ветке images_example

Накосячил с Liquibase, что делать?
- Удали все таблицы (вариант не очень грамотный, но самый простой). В терминале выполните команду <code>docker exec -it postgreGazon psql -h localhost -p 5432 -U root -W -d gazon_db</code>, введите пароль root  и нажвите enter, выполните команду <code>\dt</code>, чтобы увидеть все существующие таблицы. Для манипулирования таблицами используйте стандартный SQL-синтаксис. Чтобы удалить все таблицы, выполните команду <code>DROP SCHEMA public CASCADE;</code>, затем <code>CREATE SCHEMA public;</code>

Не работает фронт при переходе на http://localhost:8080/?
- Отображается ошибка, в конце стектрейса <code>Caused by: java.util.zip.ZipException: zip END header not found</code>. У вас по какой-то неведомой причине не скачался артефакт, необходимый Vaadin'у для генерации фронта. Подложите его самостоятельно по пути <code>C:\Users\\{Ваш_Юзер}.vaadin\node-v18.16.0-win-x64.zip</code> (.vaadin - это скрытая папка), попросите коллегу отправить вам этот файл

Ошибки при компиляции <code>cannot access...</code> или <code>cannot find symbol</code>
- Если не помогает <code>mvn clean install</code> корневого pom, попробуйте удалить папку .idea, затем в интерфейсе Идеи File -> Invalidate Caches...

Что делать, если в feign клиенте ошибка java.lang.IllegalStateException: PathVariable annotation was empty on param 0?
- Проверьте методы в Controllers. В аннотациях должно стоять имя параметра. Вот так: (@PathVariable("id") Long id);
