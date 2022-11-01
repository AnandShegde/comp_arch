import os
import subprocess
for file in os.listdir("./zips/"):
    print(file)
    subprocess.call("python2 test_zip.py "+"./zips/"+file, shell=True)