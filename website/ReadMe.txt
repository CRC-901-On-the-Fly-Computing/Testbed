
Docker container bauen und ausführen mit:

docker build -t testbed-gui .
docker run --name testbed-gui -p 8080:80 -d testbed-gui

