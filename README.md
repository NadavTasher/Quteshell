# Quteshell

Quteshell (with a spelling mistake) is a Java based TCP/IP socketed shell library, for use in any project.

## Installation

[Clone the repository](https://github.com/NadavTasher/Quteshell/archive/master.zip) or download the latest version from [Maven]() / [GitHub]()

## Usage

Opening a shell:
```java
Quteshell shell = new Quteshell(new ServerSocket(PORT).accept());
```

Basic shell server:
```java
private static final int PORT = 7000;
private static final ArrayList<Quteshell> quteshells = new ArrayList<>();

private static boolean listening = true;

public static void main(String[] args) {
    try {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (listening) {
            quteshells.add(new Quteshell(serverSocket.accept()));
        }
    } catch (Exception e) {
        System.out.println("Host - " + e.getMessage());
    }
}
```

Connecting to a shell:
```bash
nc address port
```

## Contributing
You are welcome to create pull requests and open issues!

## License
[MIT](https://choosealicense.com/licenses/mit/)