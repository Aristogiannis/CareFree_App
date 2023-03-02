import time
import subprocess
import os
import random

# Sub programmes generator
# ------------------------------

def generate_subprogrammes(n,arr,x):

    for i in range(n):

        bat_create_py = str("NUL > 'test" + str(i) + "'.py")  # string for the bat file
        py_file_alg = "import time\narr=" + str(arr) + "\nstart = time.time()\nfor j in range(" + str(i * 2000) + "," + str(len(arr) - (2000 * (n - i - 1))) + "):" + \
                      "\n    if arr[j]==" + str(x) + ":" + "\n        print(j, arr[j])\n        break\nend = time.time()\nprint(\"Programme executed in \", (end - start) * 10 ** 3, \"ms\")\ninput(\"press enter to exit\")"  # string for the sub python programmes

        with open("C:\\Users\\arist\\PycharmProjects\\parallel_programming\\generate.bat", "wt") as bat_file:  # editing the bat file to create different python programmes
            bat_file.write(bat_create_py)

        subprocess.call([r'C:\\Users\\arist\\PycharmProjects\\parallel_programming\\generate.bat'])  # calling the bat file

        with open("C:\\Users\\arist\\PycharmProjects\\parallel_programming\\test" + str(i) + ".py", "wt") as py_file:  # editing the new python files and parsing the array
            py_file.write(py_file_alg)

        os.startfile("test" + str(i) + ".py")  # running the python files
# ---------------------------


arr = []  # Array in which the wanted element is

for _ in range(10000):
    arr.append(random.randint(0,999999999))  # creating an array with random elements for testing purposes
arr[random.randint(4500,(len(arr)))] = 512   # placing the number 512 in a random index in the array in order to find it later

# ---------------------
start = time.time()
# ---------------------

length = int(len(arr))
if len(arr) < 1000:
    n = int(length//20)  # determines the number of sub-programmes that will be made
elif len(arr) < 5000:
    n = int(length // 200)
else:
    n = int(length // 2000)
x = 512  # wanted element

generate_subprogrammes(n, arr, x)

# ---------------------
end = time.time()
print("Programme executed in ", (end - start) * 10 ** 3, "ms")
# ---------------------
