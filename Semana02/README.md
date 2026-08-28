## ¿Por qué nombre y precio son val pero cantidad es var? ¿Qué pasaría si intentas cambiar el precio después de crear el producto?

1. Estas dos variables son val porque no tiene sentido que cuando estemos
   usando un carrito de un mercado/tienda se cambie el nombre o el precio de
   los productos que agregamos. Y sí hay sentido de que la cantidad cambie, ya
   que el cliente puede agregar o quitar unidades del mismo producto mientras
   arma su carrito. Además de que otro usuario podría comprar el mismo
   producto antes que nosotros.

2. Para comprobar qué pasaría, escribí producto.precio = 3000.0 después de
   haber creado el producto, y Android Studio marcó error inmediatamente en
   esa línea. Esto pasa porque precio está declarado como val, lo que lo
   convierte en una propiedad inmutable que ya no se puede modificar
   una vez que el objeto Producto se crea. En cambio, cantidad sí se puede
   cambiar porque está declarada como var, que sí permite modificación.