# NoteIT
Android Studio and Firebase dependent cloud-synced notes app for Android Devices

Developed at the Application Development Workshop of [@MSPCSRM](https://github.com/MSPCSRM "Microsoft Student Partners Club SRM on GitHub"). 

Data is fetched from firebase and updated in realtime. The firebase connection that is provided with this project is Read-only. Please make a new Firebase project and replace the google-services.json file in the app folder with your own file for full functionality.

## Setup Instructions:

1. Open Android Studio
2. Select "Checkout from Version Control"
3. In the dropdown choose Git
4. Copy paste the URL of this repository, hit Enter.
5. While the gradle build runs, open and initialise a firebase project and download the google-services.json file for the project
6. RUN! (the app)

## Project Structure:

There are 3 Activities: 
  1. MainActivity (Entry Screen, launcher activity)
  2. NotesList (Lists out all Notes in scrollable linear layout)
  3. AddNote (Create a new note/ edit existing note)

Credits:
SVGs and UI Design by [MSPC Creatives](https://msclubsrm.in/team.html#creatives)

