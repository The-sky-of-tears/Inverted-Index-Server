# Inverted Index Server

Завдання з курсової роботи по паралельним обчисленням.

## Introduction

Проект являє собою серверну частину додатку, який здатен побудувати інвертований індекс для файлів у вказаній директорії та обслуговувати задану кількість користувачів відповідаючи на їх запити щодо пошуку слів у файлах.

## Getting Started

1. Відкрийте командну строку
2. Склонуйте репозиторій за допомогою наступної команди:
   <p>git clone https://github.com/The-sky-of-tears/Inverted-Index-Server.git</p>
3. Перейдіть в папку з проектом
   <p>cd Inverted-Index_server</p>
4. Скомпілюйте код використовуючи наступну команду:
   <p>javac -d bin src/Runner.java src/server/Server.java src/index/InvertedIndex.java</p>
5. Запустіть програму. Для цього необхідно виконати наступну команду:
    <p>java -cp bin Runner [port] [directory] [threads]</p>
    
  * <em>port</em> - номер порту, який буде прослуховувати сервер і який повинні знати клієнти для підключення до нього <br>
  * <em>directory</em>  - директорія з файлами, які будуть індексовані <br>
  * <em>threads</em>  - кількість потоків, яка буде використана для індексації та опрацювання запитів клієнтів, тобто максимальна одночасна кількість користувачів <br>

6. Для завершення роботи зачиніть консоль

### Prerequisites

Перед запуском програми переконайтесь що ви встановили наступне програмне забезпечення:
 * [Java SE 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
 * [Git](https://git-scm.com/downloads)

## Usage

Для написання клієнта необхідно зважати на наступний протокол:

![Protocol](https://github.com/The-sky-of-tears/Inverted-Index-Server/assets/86189712/391df45a-900e-40e0-8b23-7f28ea3dc75d)

Приклад написання клієнта на мові Java можна найти за наступним посиланням: <br>
https://github.com/The-sky-of-tears/Inverted-Index-Client
