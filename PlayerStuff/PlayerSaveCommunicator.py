import json

def readSaveFile(fileName):
    with open(fileName) as f:
        return json.load(f)

def main():
    fileName = input("fileName : ")
    data = readSaveFile(fileName)


if __name__ == "__main__":
    main()
