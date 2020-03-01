#include <iostream>
#include <queue>
#include <mutex>
#include <condition_variable>
#include "pthread.h"
using namespace std;

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

//Semaphore setup 
// Some code from here: https://stackoverflow.com/questions/4792449/c0x-has-no-semaphores-how-to-synchronize-threads
class Semaphore{
    public:
        void Wait(int P[]);
        void Signal();
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
    queue <int[]> Q;
};

class BoundedBuffer{
    public:
        void producer();
        void Consumer();
    
    BoundedBuffer(int N){
        mutex.value = 1;
        empty.value = N;
        full.value = 0;
        int buffer[N];
    }

    void Producer(){
        //Produce item
        

        empty.Wait();
        mutex.Wait();
        
        //Add item to buffer

        mutex.Signal();
        full.Signal();
    }

    void Consumer(){
        full.Wait();
        mutex.Wait();

        //Remove item from buffer

        mutex.Signal();
        empty.Signal();

        //Use item

    }
    private:
        int buffer[];
        Semaphore mutex;
        Semaphore empty;
        Semaphore full;
};

int main(){
    
    return 0;
}