# Quteshell

Quteshell (with a spelling mistake) is a Java based TCP/IP socketed shell.

## Installation

[Clone the repository](https://github.com/NadavTasher/Quteshell/archive/master.zip), copy the files to your java project, then call `main(new String[0])` or `new Quteshell(socket).begin();`

## Usage

Opening a shell:
```java
Quteshell shell = new Quteshell(new ServerSocket(PORT).accept());
shell.begin();
```

Connecting to a shell:
```bash
nc address port
```

## Contributing
You are welcome to create pull requests and open issues!

## License
[MIT](https://choosealicense.com/licenses/mit/)