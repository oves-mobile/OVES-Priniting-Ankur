# OVES Printing App


This application able to print the provided instruction to any confired printer in the app. Sample output of the 
printer is shown below. Before printing we are doing scanning of the device, pairing to it and then passing the respective commands.
all this functionality completed with the standard Bluetooth APIs of Android SDK. for printing commands we are using OEM printing library.
all validation checks have been already implemented in this app. e.g. Bluetooth is supported on the device or not, asking for the location permission 
on devices above MarshMellow, connection, disconnection etc.

### Sample Output
It should contain like
```
123456789
ABCDEFGHIJKL
abcdefghijkl
```

- For modification of sample output, please check for `PrintTextDemo` function in `MainActivity.Java` file.

### Configure Your Printer
- Printer needs to be configured before using our app. it doesn't allow other manufacturer's printer. in short, if needs to be used other you should 
consider it for integration in our app.

1. Go To `isPrinterBounded()` function inside `MainActivity.Java` file.
2. add your printer's name there. 
e.g. 
```
if (name.contentEquals("NAME_OF_PRINTER")) {
    return device.getAddress();
}
```
3. Go to `initBroadcast()` function.
4. add your printer's name like in step 2.
i.e.
```
if (name.contentEquals("NAME_OF_PRINTER")) {
    return device.getAddress();
}
```
5. That's it! Run app and you should get the expected output.

##### Please note that your printer should be able to print physically.
 