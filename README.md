# Five nights at Annie's

Backend for a pizzeria chain order management solution, implemented using microservices.

Credits go to [Raul Cotar](gitlab.ewi.tudelft.nl/rcotar), [Borislav Semerdzhiev](gitlab.ewi.tudelft.nl/bsemerdzhiev), [Juan Tarazona](https://gitlab.ewi.tudelft.nl/jtarazonarodri), [Laurens Michielsen](gitlab.ewi.tudelft.nl/llmichielsen), [Pavlos Markesinis](gitlab.ewi.tudelft.nl/pmarkesinis), [Francisco Ruas Vaz](gitlab.ewi.tudelft.nl/fruasvaz).

## Abilities

Our solution primarily provides a way for clients to interact with the pizzerias and manage their orders and for the stores to receive these orders. On top of that, it provides a way for reginal managers to analyze and do arbitrary changes to the data managed by the system (e.g: orders, coupons).

## Structure

This project contains 4 microservices:
- authentication-microservice
- user-microservice
- food-microservice
- order-microservice

The `authentication-microservice` is responsible for authenticating andd authorizing users. After successful authentication, this microservice will provide a JWT token which can be used to prove a user's identity to the other microservices. Users never communicate directly with this server.

The `user-microservice` is responsible for registering and logging in uor clients (and managers). It acts as a proxy for the Auth server, and it also stores user data such as alergens.

The `food-microservice` handles the manu and the available foods and ingredients that are avalable for purchase. It is relied upon by the frontend as well as the next microservice.

The `order-microservice` is the biggest microservice in our system. I handles order placement/editing/deletion and all the associated checks and functionality. I communicates with the previous 2 microservices in order to ensure that oreders are processed correctly. Most of the core business logic is implemented in here (e.g: coupons, price calculation, order placement).

## Testing

Our project has a close to 100% meaningful test coverage, made possible by our extensive suite of unit and integration tests. We want ot make usre that our clients have a save and reliable solution.

## Running the microservices

You can run the two microservices individually by starting the Spring applications. There are 4 microservices: `Authentication` (start first), `User`, `Food`, and `Order`. You can manually test the system by using [Postman](https://www.postman.com/) to access the API.
