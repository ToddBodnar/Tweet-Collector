mv tweets `date +"tweets%Y%m%d%H"`

gzip `date +"tweets%Y%m%d%H"`

if [ ! -d "data/" ]; then
  mkdir data
fi

if [ ! -d `date +"data/%Y%m%d"` ]; then
  mkdir `date +"data/%Y%m%d"`
fi

mv *.gz `date +"data/%Y%m%d/"`