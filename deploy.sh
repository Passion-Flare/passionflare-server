# reference: https://tecadmin.net/kill-process-on-specific-port/

# get PID of the process running on the port
port=8081
pid=$(lsof -t -i:$port)

# kill corresponding process if exists
if [ -n "$pid" ]; then
  kill "$pid"
fi

# run in background
nohup java -jar passion-0.0.1.jar &>server.log &