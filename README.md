# Команды плагина:
#### Алиасы: /buildbattle /b /bb / battle
### Использование:
#### /battle start - начать конкурс
#### /battle pause - приостановить конкурс
#### /battle continue - продолжить конкурс
#### /battle reset - Переустановить и обнулить конкурс
#### /battle reload - Применить изменения в sttings.yml *!!! применения не изменятся если статус конкурса - включен! Работайте с файлом при выключеном конкурсе. !!!*
#### /battle extra [число] - добавить минуты к текущему таймеру.

# Фаил settings.yml
```yml
Settings:
//Назанчить тему конкурса.
  Theme: Пряничный домик
  //Назначить время для таймера конкурса.
  Time:
    hours: 0
    minutes: 60
    seconds: 60
  //Давать ли игрокам время на подготовку к конкурсу. Время для команды /plot claim /plot auto.
  preparing: 'true' 
  //Сколько давать времни на подготовку.
  preparing-time-minutes: 1
  //Переводить ли всех игрков в режим наблюдателя после конончания
  set-spectator-mode: 'true'
  //Рудимент Optional. Можно ничего сюда не писать.
  Extra-minutes: 0
  //Название прав ролей. Можно изменить.
Permissions:
  leader: buildbattle.leader
  judge: buildbattle.judge
  participant: buildbattle.participant
  //Настройка сообщений конкурса
Messages:
  battle-didnt-started: Подождите начала битвы.
  prepare-didnt-started: Подождите начала подготовки к битве.
  reload: Конфигурация перезагружена.
  already-started: Битва уже запущена!
  preparing:
    title: Билдбатл скоро начнется!
    subtitle: 'Займите плот: /plot claim'
    actionbar: 'Подготовка. До начала: %time%'
  running:
    title: Okaay let's gooo!
    subtitle: 'Тема: %theme%'
    actionbar: 'Тема: %theme% Время: %time%'
  stopped:
    title: Битва приостановлена!
    subtitle: Ожидайте продолжения.
    actionbar: Ожидайте продолжения.
  timer:
    one-hour-remaining: Остался 1 час до конца
    half-hour-remaining: Осталось 30 минут до конца
    ten-minutes-remaining: Осталось 10 минут до конца
    five-minutes-remaining: Осталось 5 минут до конца
    one-minute-remaining: Оасталась одна минута до конца
    ten seconds-countdown: Оасталось %seconds% до конца
  battle-end:
    title: Стоооооооп!
    subtitle: Битва окончена!
    actionbar: Время оценок!
  extra-time:
    title: Дополнительное время
    subtitle: + %time%
//Команды и компоненты команд которые можно использовать до конкурса.
Whitelisted-commands-and-contents:
- tp
- speed
- msg
- r
- tell
- claim
- auto
//Рудиментарный сегмент. Используется плагином для сохраниения событий конкурса.
Optional:
  Battle-status: setup
  remaining-running-hours: 0
  remaining-running-minutes: 0
  remaining-running-seconds: 0
  remaining-preparing-minutes: 0
  remaining-preparing-seconds: 0

```
