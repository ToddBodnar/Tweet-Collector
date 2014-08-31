cd ..

touch .temp_install

crontab -l >> .temp_install

echo "0,15,30,45 * * * * cd" `pwd`"; java -jar TweetCollector.jar >> /dev/null"  >> .temp_install
echo "5 * * * * cd" `pwd`"; bash scripts/move.sh" >> .temp_install

cat .temp_install | crontab -

rm .temp_install

crontab -l

echo "Finished"