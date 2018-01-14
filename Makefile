# Birthday
encrypt:
	tar --create --file - --gzip -- php | openssl aes-256-cbc -salt -out php.crypt; \
	rm -rf php;

	tar --create --file - --gzip -- DezifyDatabase | openssl aes-256-cbc -salt -out DezifyDatabase.crypt; \
	rm -rf DezifyDatabase;

decrypt:
	openssl aes-256-cbc -d -salt -in php.crypt | tar -v --extract --gzip --file -; \
	rm -rf php.crypt;

	openssl aes-256-cbc -d -salt -in DezifyDatabase.crypt | tar -v --extract --gzip --file -; \
	rm -rf DezifyDatabase.crypt;
