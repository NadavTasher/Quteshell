# Quteshell

Quteshell (with a spelling mistake) is a Java based TCP/IP socketed shell library, for use in any project.

## Installation

[Clone the repository](https://github.com/NadavTasher/Quteshell/archive/master.zip) or download / include the latest version from [Maven](https://search.maven.org/artifact/org.quteshell/quteshell/) 

## Usage

Opening a shell:
```java
Shell shell = new Shell(new ServerSocket(PORT).accept(), new Configuration());
```

Basic shell server:
```java
private static final int PORT = 7000;
private static final ArrayList<Shell> shells = new ArrayList<>();

private static boolean listening = true;

public static void main(String[] args) {
    Configuration configuration = new Configuration();
    try {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (listening) {
            shells.add(new Shell(serverSocket.accept(), configuration));
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
[MIT License](https://choosealicense.com/licenses/mit/)