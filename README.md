# Weight Tracker Android Widget

A simple Android home screen widget designed for quick daily weight logging. It sends data to a private Google Sheet via a Google Apps Script API.

## How it Works
1.  **Widget Display:** Shows the last recorded weight stored in local preferences.
2.  **Input Popup:** Tapping the widget opens a lightweight dialog activity.
3.  **Data Sync:** On "Save", the weight is sent via a POST request to the linked API and the widget is updated.

## Tech Stack
* **Language:** Kotlin
* **UI:** RemoteViews (Widget) & XML Layouts
* **Networking:** HttpURLConnection (Native)

## Setup
1.  Clone this repository.
2.  Open in **Android Studio**.
3.  Create/Open `local.properties` in the project root.
4.  Add your API URL:
    `API_URL="https://script.google.com/macros/s/YOUR_ID/exec"`
5.  Build and install the app on your device.
6.  Add the "Weight Tracker" widget to your home screen.

## Related Repository
This widget requires the [weight-tracker-api](https://github.com/Tom-Les/weight-tracker-api) backend to function.
