#include <iostream>
#include <mutex>
#include <condition_variable>
#include <chrono>
#include <thread>
#include <ctime>
#include <vector>
#include <pthread.h>
using namespace std;

/* Choose from below presets for different modes 
*     They change the N value and the max value for sleep functions
*  Uncomment one block to activate mode
*/

//Rapid chaos
// #define N 20
// #define maxLength 1000
// #define prodMaxWait 100

//Regular
#define N 3
#define maxLength 5000
#define prodMaxWait 3000

//Slow
// #define N 3
// #define maxLength 13000
// #define prodMaxWait 9000



//Semaphore setup 
// Some code from here: https://stackoverflow.com/questions/4792449/c0x-has-no-semaphores-how-to-synchronize-threads
class Semaphore{
    public:
        int value;
    
    Semaphore(){
        value = 0;
        //Queue is empty
    }

    void Wait(){
        std::unique_lock<std::mutex> lock(mtx);
        while(value == 0){
            cv.wait(lock);
        }
        value--;
    }

    void Signal(){
        std::unique_lock<std::mutex> lock(mtx);
        value++;
        cv.notify_one();
    }

    private:
    std::mutex mtx;
    std::condition_variable cv;
};

/*
Master thread (producer):
 - Listens for requests, inserts them into request queue
 - Sleep for random duration and produce a request
 - Each request has sequentially increasing ID and random length
 - Insert request into queue and go back to sleep
 - If queue is full, must wait before inserting request
Slave threads (consumers):
 - On new arrival into request queue, idle slave thread takes the
    request and processes it.
 - Slave threads either idle or busy, if idle, consume next thread
    on queue.
 - Will be busy for a duration equal to the request
    length of that request.
Request queue:
 - Circular buffer of size N
*/
class BoundedBuffer{
    public:
        int id;
        //Multidimensional arrays as class variables is hard, so I use 2 arrays: 1 for ids one for lengths
        std::vector<int> idVec;
        std::vector<int> lengthVec;
        int idOffset;
        int lengthOffset;

    BoundedBuffer(int n){
        mutex.value = 1;
        empty.value = n;
        id = 0;
        full.value = 0;
        idVec.resize(n);
        lengthVec.resize(n);
        idOffset = 0;
        lengthOffset = 0;
    }

    void Producer(){
        //Get mutex
        empty.Wait();
        mutex.Wait();

        //Generate request and add to queue (increment queueVal and id)
        idVec.insert(idVec.begin()+idOffset, id);
        lengthVec.insert(lengthVec.begin()+lengthOffset, rand()%maxLength);
        idOffset++;
        lengthOffset++;
        id++;
        const time_t ctt = time(0);
        std::cout << "\033[1;31mProducer\033[0m added request: ID=" << idVec[idOffset-1] << ", length=" << lengthVec[lengthOffset-1] << "\n";
        
        //Release mutex
        mutex.Signal();
        full.Signal();

        return;
    }

    std::array<int, 2> Consumer(){
        //Get mutex
        full.Wait();
        mutex.Wait();

        //Remove item from buffer
        int itemId = idVec[idOffset-1];
        int itemTime = lengthVec[lengthOffset-1];
        idOffset--;
        lengthOffset--;
        std::cout << "\033[1;34mConsumer\033[m reading request: ID=" << itemId << ", length=" << itemTime <<"\n";

        //Release mutex
        mutex.Signal();
        empty.Signal();

        //Return itemdId and itemTime to slave thread
        std::array<int, 2> r = {itemId, itemTime};
        return r;
    }
    private:
        Semaphore mutex;
        Semaphore empty;
        Semaphore full;
};

//Global object of boundedBuffer for use in standalone functions
BoundedBuffer bb(N);

//Called by master threads
static void *master(void *arg){
    std::cout << "Starting master on thread " << pthread_self() << "\n";

    //Loop forever
    while(true){
        //Call producer
        bb.Producer();

        //Sleep for random amount of time (<1 sec) and restart
        int prodSleep = rand()%prodMaxWait;
        std::cout << "Producer: sleeping for " << prodSleep << " ms\n";
        std::this_thread::sleep_for(std::chrono::milliseconds(prodSleep));
    }
}

//Called by slave threads
static void *slave(void *arg){
    std::cout << "starting idle slave on thread " << pthread_self() << "\n"; //(will sometimes at the same time due to multithreading and this instruction not being run atomically)

    //Loop forever
    while(true){
        //While queue is empty, be idle
        while(bb.idOffset == 0){
            std:this_thread::sleep_for(std::chrono::milliseconds(100));
        }

        //Queue not empty
        std::array<int, 2> threadInfo = bb.Consumer();
        int itemId = threadInfo[0];
        int itemLength = threadInfo[1];
        pthread_t threadId = pthread_self();

        //Use item (put to sleep for itemTime)
        std::this_thread::sleep_for(std::chrono::milliseconds(itemLength));
        std::cout << "Consumer " << threadId << ": completed request ID " << itemId << "\n";
        
    }
}

int main(){
    pthread_t threads[N];
    int rc;
    intptr_t id;

    //Fork and run consumer on N threads
    for(int i=0; i<N; i++){
        id = (intptr_t) i;
        rc = pthread_create(&threads[i], NULL, slave, (void *)id);
        if(rc){
            cout << "Error in creating thread " << rc << endl;
            exit(-1);
        }
    }

    //Run producer on master thread
    master(NULL);

    pthread_exit(NULL);
}