# FlintExample
This repository contains example Java programs designed to run on **FlintJVM**.  
It also demonstrates how to build, flash, and debug Java applications on ESP32 using the **FlintJVMDebug** VS Code extension.
These projects can run well on ESP32.
## Prerequisites
Before you start, make sure you have installed:
- Flash `FlintESPJVM` firmware to your ESP32 board. You can use [ESP Web Tool](https://esp.flint.vn/) to support flash
- Use vscode and install [`FlintJVM Debug`](https://marketplace.visualstudio.com/items?itemName=ElectricThanhTung.flintjvm-debugger) extension.
- Make sure you have javac (java compiler) installed and can call it in terminal window. You can also run the command `javac --version` to check.
## How to run example:
- Open an example you want to run in vscode. For example, open the **FlintExample/examples/FlintIO/Pin** folder.
- Update the `port` parameter in the **launch.json** file to match the actual port connected to your board.
- To compile the project, click `Terminal > Run Build Task...` or press `Ctrl` + `Shift` + `B`. After successful compilation, **bin/Main.class** file will be created.
- Click `Run > Start Debugging` or press `F5` to run and debug the project.

*You can also open the entire project as **Multi-root Workspaces** by opening the **FlintExample.code-workspace** file.*
