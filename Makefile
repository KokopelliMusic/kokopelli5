backend:
	quarkus dev

install:
	cd src/web && npm i

frontend: install
	npm run dev --prefix ./src/web