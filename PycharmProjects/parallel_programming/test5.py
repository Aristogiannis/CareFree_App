import time
start = time.time()
for j in range(1000000,100000):
    if arr[j]==512:
        print(j, arr[j])
        break
end = time.time()
print("Programme executed in ", (end - start) * 10 ** 3, "ms")
input("pause")