# android-paging-failure-example

A simple demonstration of a failure case in the current versions of the andorid paging libraries;
that is 'androidx.paging:paging-common-ktx:3.1.1' and 'androidx.paging:paging-compose:1.0.0-alpha17'

## Usage

After loading the project in Android Studio and starting the app on a device or emulator of your choice,
either push the FAB stating 'Go to #500' or manually scroll rapidliy through the list.

If the currently viewed item is more than roughly twice the jump distance from the last loaded page key, loading stops completely.
(In some rare cases, which I suspect are related to using maxSize, loading instead entered an infinite loop.)

-----

Please excuse the gradle scrip being slightly messy, but I believe the actual source code should be clear enough to demonstrate the issue.
