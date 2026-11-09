
# OIBSIP: Oasis Infobyte Student Internship Program

**Intern:** Momina Raheel
**Track:** Android App Development
**Tasks completed:** 3 of 3 required (Task 1, Task 3, Task 5)
**Tech:** Java, XML, Android Studio

This repository contains all my task submissions for the Oasis Infobyte Student Internship Program (OIBSIP). Every project is a standalone Android app. Each one meets the feature checklist given for its task, and there is no backend or external library in any of them.

---

## Table of Contents

1. [Submitted Tasks](#submitted-tasks)
2. [Task 1: Unit Converter](#task-1--unit-converter)
3. [Task 3: Calculator](#task-3--calculator)
4. [Task 5: Stopwatch](#task-5--stopwatch)
5. [Screenshots](#screenshots)
6. [Demo Videos and LinkedIn Posts](#demo-videos-and-linkedin-posts)
7. [Tech Stack](#tech-stack)
8. [How to Run Any Project](#how-to-run-any-project)
9. [Repository Structure](#repository-structure)
10. [Author](#author)

---

## Submitted Tasks

| Task | Project | Folder | Summary |
|---|---|---|---|
| Task 1 | Unit Converter | [`Android-Task1-UnitConverter`](./Android-Task1-UnitConverter) | Converts length, weight and temperature using dropdowns |
| Task 3 | Calculator | [`Android-Task3-Calculator`](./Android-Task3-Calculator) | Basic arithmetic with a button grid and error handling |
| Task 5 | Stopwatch | [`Android-Task5-Stopwatch`](./Android-Task5-Stopwatch) | Start, stop, reset and lap with lifecycle handling |

---

## Task 1 · Unit Converter

**Folder:** `Android-Task1-UnitConverter`

An app that converts values between common units of measurement across three categories.

### Features

| Feature | Details |
|---|---|
| Value input | Numeric field that accepts decimals and negative numbers |
| Source unit | Dropdown (Spinner) to choose the unit to convert from |
| Target unit | Dropdown (Spinner) to choose the unit to convert to |
| Convert button | Calculates and displays the result |
| Result display | Full conversion with unit labels, for example `100 Centimetre = 1 Metre` |
| 3 categories | Length, Weight and Temperature |
| Category selector | Switching category reloads both unit dropdowns and clears the old result |
| Input validation | A Toast message appears if the field is empty or not a valid number |
| Absolute zero check | A Toast message appears for temperatures below absolute zero |

### Supported Units

| Category | Units |
|---|---|
| Length | Millimetre, Centimetre, Metre, Kilometre, Inch, Foot, Yard, Mile |
| Weight | Milligram, Gram, Kilogram, Pound, Ounce |
| Temperature | Celsius, Fahrenheit, Kelvin |

### How It Works

- **Length and weight:** every unit has a factor relative to a base unit (metre for length, gram for weight). The input is multiplied by the source factor to get the base value, and then divided by the target factor.
- **Temperature:** the value is converted to Celsius first, and then from Celsius into the target unit.
- Results are rounded to 4 decimal places, with trailing zeros removed.
- A listener on the category Spinner refills both unit Spinners whenever the category changes.

### Test Cases Checked

| Input | Expected result |
|---|---|
| 100 Centimetre to Metre | 1 |
| 30 Millimetre to Centimetre | 3 |
| 0 Celsius to Fahrenheit | 32 |
| 100 Celsius to Kelvin | 373.15 |
| Empty input, tap Convert | Toast: "Please enter a value" |
| -300 Celsius | Toast: "Below absolute zero is not possible" |

---

## Task 3 · Calculator

**Folder:** `Android-Task3-Calculator`

A clean calculator that performs basic arithmetic through a responsive button grid.

### Features

| Feature | Details |
|---|---|
| Display | Shows the current input and result, with a smaller line for the running expression |
| Number input | Digits 0-9 and a decimal point (only one decimal point per number) |
| Operators | Addition (+), subtraction (−), multiplication (×), division (÷) |
| Equals (=) | Evaluates the current operation |
| Clear (C) | Resets the calculator completely |
| Backspace (⌫) | Deletes the last entered digit |
| Division by zero | Shows **"Error"** instead of crashing |
| Operator chaining | Calculates step by step, so `5 + 3 × 2` is evaluated left to right as `(5 + 3) × 2 = 16` |
| Grid layout | Buttons arranged with `GridLayout` in XML |
| Stability | No crashes on rapid or repeated button taps |

### How It Works

- The app tracks the current input, the first operand, and the pending operator.
- Pressing an operator first calculates any pending operation, which is what makes chaining work without pressing `=` each time.
- Division by zero is detected inside the calculation method, and the display switches to an Error state. Pressing any digit, `C`, or `⌫` recovers from it.
- Results are formatted with `BigDecimal`, so `0.1 + 0.2` shows as `0.3`, and trailing zeros are removed.
- Input is limited to 15 digits to keep the display readable.
- All buttons use `OnClickListener`s set in Java, with no logic in the XML.

### Test Cases Checked

| Input | Expected result |
|---|---|
| `5 × 6 =` | 30 |
| `5 + 3 × 2 =` | 16 |
| `7 ÷ 0 =` | Error |
| `0.1 + 0.2 =` | 0.3 |
| Type `12`, then `⌫` | 1 |
| Rapid tapping of many buttons | No crash |

---

## Task 5 · Stopwatch

**Folder:** `Android-Task5-Stopwatch`

A stopwatch with start, stop and reset controls and a lap recorder. It stays accurate when the app goes into the background.

### Features

| Feature | Details |
|---|---|
| Large time display | Shown in `HH:MM:SS` format |
| Start | Begins from zero, or continues from the paused time |
| Stop | Freezes the timer at the current elapsed time |
| Reset | Stops the timer, sets the display to `00:00:00` and clears the laps |
| Button states | Start is disabled while running. Stop and Lap are disabled while stopped |
| Lifecycle handling | Correct time is kept when the user leaves and returns to the app (`onPause` and `onResume`) |
| Lap recorder (bonus) | A Lap button saves the current time (with hundredths of a second) to a scrollable list, newest lap first |

### How It Works

- A `Handler` runs a `Runnable` every 50 milliseconds on the UI thread to refresh the display.
- Elapsed time is calculated from `SystemClock.elapsedRealtime()` minus the start time, rather than by counting ticks, so it stays accurate even if UI updates are delayed.
- When stopped, the elapsed time is saved, and pressing Start again continues from that value.
- `onPause()` stops UI updates in the background. `onResume()` restarts them, and the display jumps straight to the correct value.
- Buttons are enabled and disabled based on the running state, which prevents invalid actions such as starting twice.

### Test Cases Checked

| Action | Expected result |
|---|---|
| Tap Start | Time runs, Start is greyed out, Stop is active |
| Tap Lap while running | A new lap appears at the top of the list |
| Tap Stop | Time freezes |
| Tap Start after Stop | Continues from the stopped time, not from zero |
| Press Home, wait, reopen the app | Time kept counting correctly |
| Tap Reset | Display returns to 00:00:00 and laps are cleared |

---

## Screenshots

Screenshots of every app are in the [`screenshots`](./screenshots) folder.

| App | Screenshot |
|---|---|
| Unit Converter | `screenshots/unit-converter.png` |
| Calculator | `screenshots/calculator.png` |
| Stopwatch | `screenshots/stopwatch.png` |

---

## Demo Videos and LinkedIn Posts

| Task | Demo video / LinkedIn post |
|---|---|
| Task 1: Unit Converter | [YOUR-LINKEDIN-POST-LINK](https://www.linkedin.com/) |
| Task 3: Calculator | [YOUR-LINKEDIN-POST-LINK](https://www.linkedin.com/) |
| Task 5: Stopwatch | [YOUR-LINKEDIN-POST-LINK](https://www.linkedin.com/) |

---

## Tech Stack

- **Language:** Java
- **UI:** XML layouts (`LinearLayout`, `GridLayout`, `Spinner`, `ListView`)
- **IDE:** Android Studio
- **Build system:** Gradle (Kotlin DSL)
- **Testing:** Android Emulator (Pixel 7, API 34)
- **No backend and no external libraries**

---

## How to Run Any Project

1. Clone the repository:
   ```bash
   git clone https://github.com/shadowMomina/OIBSIP.git
   ```
2. Open **Android Studio** and choose **File → Open**.
3. Select one project folder, for example `Android-Task3-Calculator`.
4. Wait for the Gradle sync to finish.
5. Click **Run ▶** and choose an emulator or a connected Android device.

---

## Repository Structure

```
OIBSIP/
├── Android-Task1-UnitConverter/
│   └── app/src/main/
│       ├── java/com/hana/unitconverter/MainActivity.java
│       └── res/layout/activity_main.xml
├── Android-Task3-Calculator/
│   └── app/src/main/
│       ├── java/com/hana/calculatorapp/MainActivity.java
│       └── res/layout/activity_main.xml
├── Android-Task5-Stopwatch/
│   └── app/src/main/
│       ├── java/com/hana/stopwatch/MainActivity.java
│       └── res/layout/activity_main.xml
├── screenshots/
└── README.md
```

---

## Author

**Momina Raheel**
Android App Development Intern, Oasis Infobyte
GitHub: [shadowMomina](https://github.com/shadowMomina)
LinkedIn: [YOUR-LINKEDIN-PROFILE-LINK](https://www.linkedin.com/)

#oasisinfobyte #androiddev #java #internship
