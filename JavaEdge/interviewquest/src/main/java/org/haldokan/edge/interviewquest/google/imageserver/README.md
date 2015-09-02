Implementation of an image cache server. It processes user requests concurrently and keeps in cache only the N most
recently access images.

There are a few design and implementation challenges that are resolved:
- Multiple requests for the same image that is not cached and has to be loaded from persistent storage. Request threads
  have to be 'parked' and notified once the image become available in the cache. With java8 I could have used
  ConcurrentHashMap.computeIfAbsent instead of actually implementing it.

- Loading images have to be done concurrently which leads to storing 'Future' images in the cache instead of the fully
  loaded images.

- Trimming the cache size to the N most recently accessed images for the multi-threaded cache proved not an easy task.
  Using LinkedHashMap.removeEldestElement does not help in this context because manipulating the LinkedHashMap require
  synchronization. Also using access-based LinkedHashSet requires implicitly maintaining a sorted data structure that must be
  updated at each access which is very inefficient.
  I ended up using a maintenance thread that is scheduled to trim the cache to size at intervals. The accessed images are
  stored in a LinkedHashMap that work like a queue except that before adding an element we remove if it exists (constant
  time in a hashing data structure).

- Still to be done handling exceptions when the futures stored in the cache throw exceptions. We want to remove the cache
  entries for such images.
