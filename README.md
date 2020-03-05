
===================================================
|| Members                            Student ID ||
|| Will McFarland                     86184900   ||
|| Alvin Krisnanto Putra              54658380   ||
|| (Winter) Manassawin Rotsawatsuk    12682936   ||
||                                               ||
|| Group 14                                      ||
===================================================
Member contributions:
Will:
    - Wrote C++ implementation
    - Wrote C++ parts of README
    - Code testing
Alvin:
    - Wrote Java version base
    - Code testing
Winter:
    - Fixed Java version
    - Document Java version
    - Code testing

Design Choices - C++
Semaphore Class: implements semaphores by using the lock functionality in C++. This is required because C++ does not natively support semaphores.
BoundedBuffer class: implements producer and consumer functions using the semaphore class. It contains a queue that stores the id of each request and the corresponding length. The queue was implemented with 2 vectors and an integer offset. The offset keeps track of where the “head” is, or the index of the value that was last added and next to be removed.
Global instantiation: A global instantiation of BoundedBuffer is created in order for its functions and attributes to be used by master and slave functions. In the instantiation, 3 functions are called to acquire parameters as input from the user. The parameters collected are the max producer wait (maximum time in ms that the producer will wait before generating another request), max length (max length of request in ms), and N (number of slave threads = max size of queue)
Master function: Only called once. On starting, it will enter an endless loop where 2 things happen each iteration: the producer function is called, then the thread will sleep for a length determined by the input parameter “maxProdWait”.
Slave function: Like the master function, on creation it will enter an infinite loop. In that loop, the program will enter another loop until the offset is set to a number other than 0. In other words, the slave function will not continue until there is something on the queue to consume (slave thread is idle). Once the offset does not equal 0, the function will call the consumer function, and using its output (a 2-element array containing id and length of request), the thread will sleep for an amount of time equal to the length of the request.
Main function: Will first create N slave threads using pthread_create, with each thread executing the slave function. Finally the master function is called. Now, all slave threads and master should be running concurrently on different threads.

Design Choices - Java
There are about 5 main classes needed in order for the project to be executed properly. The first is the base class which contains the main function. The base class handles receiving input from the user through the command line and uses the input to startup a master object. The master object then starts up its own thread with the given parameters. The master object starts by creating a buffer object which will create the required number of request objects and will then wait for further instructions. The master object then creates the required number of slave threads and starts sending requests to the buffer object. The slave objects loop indefinitely waiting for requests to be made in the buffer and takes them once a request is available. Once done with the request, the slave object goes back to waiting for a request in the buffer. 

The Base class uses while loops in order to ensure the proper input is given by the user and prints useful messages depending on the input given.
The Buffer class contains methods with the synchronized tag which allows the the threads to access the class properly without conflicts. Due to the fact that reading and writing share one lock, the wait method can’t be used for both full buffers and empty buffers. We instead feed the slave a null object, which means that in the case of an empty buffer, the slaves will still waste time slices to pull null objects instead of waiting for signal.
The Master class and Slave class contain threads which allow the individual objects to run in parallel.

Compiling and Running in Linux - C++
    Compile main.cpp using following command: “g++ -pthread -o main main.cpp”
    Now run the file using “./main”
The program will then ask for 3 parameters: Max producer wait, max length, and N. All should be entered as integers.

Compiling and Running in Linux - Java
This code was written in JDK-13.0.2, and therefore JDK-13.0.2 should be used.
1. Download JDK-13.0.2 (https://www.oracle.com/java/technologies/javase-jdk13-downloads.html#license-lightbox)
2. Extract it and move it using:
computer@ubuntu:$ sudo mv jdk-13.0.2 /opt/
3. Set JAVA_HOME and PATH in Ubuntu profile using:
computer@ubuntu:$ sudo tee /etc/profile.d/jdk13env.sh <<EOF
export JAVA_HOME=/opt/jdk-13.0.2
export PATH=$PATH:$JAVA_HOME/bin
EOF
and then:
computer@ubuntu:$ source /etc/profile.d/jdk13env.sh
4. With JDK-13, the .java files can be compiled. Move to the directory with the 5 .java files and use:
computer@ubuntu:$ javac Base.java Buffer.java Master.java Request.java Slave.java
5. After compiling, it can be run using:
computer@ubuntu:$ java Base.java
6. Input the buffer size, maximum request length, and maximum time gap between request generation.

Discussion:
The java implementation was quite straightforward. Java provides classes and interfaces which makes creating threads and using them a matter of knowing what commands to use and when to use them. Our IDE also allows us to easily debug our program using breakpoints and variable watching. 

The C++ implementation required more setup in terms of the amount of code required to allow for the producer and consumer functionality. Since C++ does not natively support semaphores, they needed to be implemented manually. It was also a great challenge to learn a new language for such a complex project. C++ is difficult in the sense that almost everything must be created, and very little is supplied by the language in terms of native functions and libraries, however the idea of creating and fine-tuning every aspect of the program is quite fun and satisfying.
