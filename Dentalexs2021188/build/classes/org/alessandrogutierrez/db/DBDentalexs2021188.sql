Drop database if exists DBDentalexs2021188;
Create database DBDentalexs2021188;

Use DBDentalexs2021188;

Create table Pacientes (
	codigoPaciente int not null,
    nombresPaciente varchar (50) not null,
    apellidosPaciente varchar (50) not null,
    sexo char not null,
    fechaNacimiento date not null,
    direccionPaciente varchar (100) not null,
    telefonoPersonal varchar (8) not null,
    fechaPrimeraVisita date,
    primary key PK_codigoPaciente (codigoPaciente)
);

Create table Especialidades (
	codigoEspecialidad int not null auto_increment,
    descripcion varchar (100) not null,
    primary key PK_codigoEspecialidad (codigoEspecialidad)
);

Create table Medicamentos (
	codigoMedicamento int not null auto_increment,
    nombreMedicamento varchar (100) not null,
    primary key PK_codigoMedicamento (codigoMedicamento)
);

Create table Doctores (
	numeroColegiado int not null,
    nombresDoctor varchar (50) not null,
    apellidosDoctor varchar (50) not null,
	telefonoContacto varchar (8) not null,
    codigoEspecialidad int not null,
    primary key PK_numeroColegiado (numeroColegiado),
    constraint FK_Doctores_Especialidades foreign key (codigoEspecialidad)
		references Especialidades (codigoEspecialidad)
);

Create table Recetas (
	codigoReceta int not null auto_increment,
    fechaReceta date not null,
    numeroColegiado int not null,
    primary key PK_codigoReceta (codigoReceta),
    constraint FK_Recetas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);			

Create table DetalleReceta (
	codigoDetalleReceta int not null auto_increment,
    dosis varchar (100) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_codigoDetalleReceta (codigoDetalleReceta),
    constraint FK_DetalleReceta_Receta foreign key (codigoReceta)
		references Recetas (codigoReceta),
	constraint FK_DetalleReceta_Medicamentos foreign key (codigoMedicamento)
		references Medicamentos (codigoMedicamento)
);

Create table Citas (
	codigoCita int not null auto_increment,
    fechaCita date not null,
    horaCita time not null,
    tratamiento varchar (150),
    descripCondActual varchar (255) not null,
    codigoPaciente int not null,
    numeroColegiado int not null,
    primary key	PK_codigoCita (codigoCita),
    constraint FK_Citas_Pacientes foreign key (codigoPaciente)
		references Pacientes (codigoPaciente),
	constraint FK_Citas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

-- ----------------------------------------------------------------------------------------------

-- $-------------------- Procedimientos Almacenados --------------------$

-- $-------------------- Pacientes --------------------$

-- #-------------------- Agregar Paciente --------------------#
Delimiter $$
	Create procedure sp_AgregarPaciente (in codigoPaciente int, in nombresPaciente varchar (50), 
		in apellidosPaciente varchar (50),in sexo char,in fechaNacimiento date,
			in direccionPaciente varchar (100),in telefonoPersonal varchar(8),in fechaPrimeraVisita date)
		Begin
			Insert into Pacientes (codigoPaciente, nombresPaciente, apellidosPaciente, sexo, 
				fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita)
                values (codigoPaciente, nombresPaciente, apellidosPaciente, upper(sexo), 
				fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita);
        End $$
Delimiter ;

call sp_AgregarPaciente (1001, 'Pedro Manuel', 'Armas Chang', 'm','1982-08-17','Zona 1 Mixco','45632148',now() );
call sp_AgregarPaciente (1002, 'Alessandro Emanuel', 'Gutiérrez Boc', 'm','2005-03-20','Zona 6 Mixco','52320011',now() );
call sp_AgregarPaciente (1003, 'Jose Leonel', 'Pirir Asturias', 'm','2005-03-20','Zona 4 Mixco','10603214',now() );
call sp_AgregarPaciente (1004, 'Pedro Manuel', 'Armas Chang', 'm','1982-08-17','Zona 8 Mixco','20010336',now() );
call sp_AgregarPaciente (1005, 'Denis', 'Casas', 'm','1992-08-17','Zona 13','54213636',now() );
call sp_AgregarPaciente (1006, 'Mario', 'Martinez', 'm','2000-08-15','Zona 4 de mixco','12214785',now() );
call sp_AgregarPaciente (1007, 'Cristian', 'Diaz', 'm','2000-03-15','Zona 8 de mixco','25361010',now() );
call sp_AgregarPaciente (1008, 'Mario', 'Martinez', 'm','2010-08-15','Zona 4 de mixco','12214785',now() );
call sp_AgregarPaciente (1009, 'Diego', 'Monzon', 'm','2008-08-15','Zona 7 de mixco','11200054',now() );
call sp_AgregarPaciente (1010, 'Hernesto', 'Pinzon', 'm','1987-06-30','Zona 1 de mixco','10526987',now() );
call sp_AgregarPaciente (1011, 'Hugo', 'Luz', 'm','1981-09-10','Zona 9 de mixco','58692121',now() );
-- #-------------------- Listar Paciente --------------------#
Delimiter $$
	Create procedure sp_ListarPacientes()
		Begin
			Select
				P.codigoPaciente, 
                P.nombresPaciente, 
                P.apellidosPaciente, 
                P.sexo, 
                P.fechaNacimiento, 
                P.direccionPaciente, 
                P.telefonoPersonal, 
                P.fechaPrimeraVisita
			From Pacientes P;
        End $$
Delimiter ;

call sp_ListarPacientes();

-- #-------------------- Buscar Paciente --------------------#
Delimiter $$
	Create procedure sp_BuscarPaciente (in codPaciente int)
		Begin 
			Select 
				P.codigoPaciente, 
                P.nombresPaciente, 
                P.apellidosPaciente, 
                P.sexo, 
                P.fechaNacimiento, 
                P.direccionPaciente, 
                P.telefonoPersonal, 
                P.fechaPrimeraVisita
			From Pacientes P
				where codigoPaciente = codPaciente;
        End $$
Delimiter ;

call sp_BuscarPaciente (1001);

-- #-------------------- Eliminar Paciente --------------------#
Delimiter $$
	Create procedure sp_EliminarPaciente (in codPaciente int)
		Begin
			Delete from Pacientes
				where codigoPaciente = codPaciente;
        End $$
Delimiter ;

call sp_EliminarPaciente (1001);
call sp_ListarPacientes();

-- #-------------------- Editar Paciente --------------------#
Delimiter $$
	Create procedure sp_EditarPaciente (in codPaciente int,in nomPaciente varchar (50),
		in apePaciente varchar (50), in sx char,in fechaNac date,in dirPaciente varchar (100),
		in telPersonal varchar (8),in fechaPV date)
        Begin
			Update Pacientes P
				set
					P.nombresPaciente = nomPaciente, 
                    P.apellidosPaciente = apePaciente, 
                    P.sexo = sx, 
                    P.fechaNacimiento = fechaNac, 
                    P.direccionPaciente = dirPaciente, 
                    P.telefonoPersonal = telPersonal, 
                    P.fechaPrimeraVisita = fechaPV
                    where codigoPaciente = codPaciente;
        End $$
Delimiter ;

-- $-------------------- Especialidades --------------------$

-- #-------------------- Agregar Especialidad --------------------#
Delimiter $$
	Create procedure sp_AgregarEspecialidad(in descripcion varchar(100))
		Begin
			Insert into Especialidades (descripcion) value (descripcion);
		End$$
Delimiter ;
call sp_AgregarEspecialidad('Endodoncista');
call sp_AgregarEspecialidad('Odontopediatra');
call sp_AgregarEspecialidad('Cirugía maxilofacial y oral');
call sp_AgregarEspecialidad('Ortodoncista');
call sp_AgregarEspecialidad('Endodoncista');
call sp_AgregarEspecialidad('Radiología maxilofacial y oral');
call sp_AgregarEspecialidad('Prostodoncista');
call sp_AgregarEspecialidad('Odontólogo General');
call sp_AgregarEspecialidad('Cirujano oral');
call sp_AgregarEspecialidad('Patología oral y maxilofacial');
-- #-------------------- Listar Especialidad --------------------#
Delimiter $$
	Create procedure sp_ListarEspecialidades()
		Begin
			Select 
				E.codigoEspecialidad,
				E.descripcion
				from Especialidades E;
		End$$
Delimiter ;
call sp_ListarEspecialidades();	

-- #-------------------- Buscar Especialidad --------------------#
Delimiter $$
	Create procedure sp_BuscarEspecialidad(in codEs int)
		Begin
			Select
				E.codigoEspecialidad,
                E.descripcion
					from Especialidades E where codEs=codigoEspecialidad;
		End$$
Delimiter ;
call sp_BuscarEspecialidad(1);

-- #-------------------- Eliminar Especialidad --------------------#
Delimiter $$
	Create procedure sp_EliminarEspecialidad(in codEsp int)
		Begin 
			Delete from Especialidades
				where codEsp=codigoEspecialidad;
		End$$
Delimiter ;
call sp_EliminarEspecialidad(1);
call sp_ListarEspecialidades();

-- #-------------------- Editar Especialidad --------------------#
Delimiter $$
	Create procedure sp_EditarEspecialidad(in codEsp int, in descri varchar(100))
		Begin
			Update Especialidades E
				set
					E.descripcion=descri
					where codEsp=codigoEspecialidad;
		End$$
Delimiter ;
call sp_EditarEspecialidad(2,'Cirujano');
call sp_ListarEspecialidades();

-- $-------------------- Medicamentos --------------------$
-- #-------------------- Agregar Medicamento --------------------#

Delimiter $$
	Create procedure sp_AgregarMedicamento(in nombreMedicamento varchar(100))
		Begin
			Insert into Medicamentos(nombreMedicamento)
				value (nombreMedicamento);
		End$$
Delimiter ;
call sp_AgregarMedicamento ('Acetaminofen');
call sp_AgregarMedicamento('IRS');
call sp_AgregarMedicamento('Ibuprofeno');
call sp_AgregarMedicamento('Amoxicilina ');
call sp_AgregarMedicamento('Anestesia');
call sp_AgregarMedicamento('IRS');
call sp_AgregarMedicamento('Analgésicos');
call sp_AgregarMedicamento('Homeopatía');
call sp_AgregarMedicamento('Laser terapéutico');
call sp_AgregarMedicamento('Pastillas Dolor');

-- #-------------------- Listar Medicamento --------------------#
Delimiter $$
	Create procedure sp_ListarMedicamentos()
		Begin 
			Select
				M.codigoMedicamento,
                M.nombreMedicamento
				from Medicamentos M;
		End $$
Delimiter ;
call sp_ListarMedicamentos();	

-- #-------------------- Buscar Medicamento --------------------#
Delimiter $$
	Create procedure sp_BuscarMedicamento(in codMedicam int)
		Begin 
			Select 
				M.codigoMedicamento,
                M.nombreMedicamento
					from Medicamentos M where codMedicam=codigoMedicamento;
		End $$
Delimiter ;
call sp_BuscarMedicamento(1);

-- #-------------------- Eliminar Medicamento --------------------#
Delimiter $$
	Create procedure sp_EliminarMedicamento(in codMedi int)
		Begin
			Delete from Medicamentos 
				where codMedi=codigoMedicamento;
		End$$
Delimiter ;
call sp_EliminarMedicamento(2);
call sp_ListarMedicamentos();

-- #-------------------- Editar Medicamento --------------------#
Delimiter $$
	Create procedure sp_EditarMedicamento(in codMedi int,in nombreMedi varchar(100))
		Begin 
			Update Medicamentos M set
				M.nombreMedicamento=nombreMedi
					where codMedi = codigoMedicamento;
		End$$
Delimiter ;
call sp_EditarMedicamento(1,'IRS');			

-- $-------------------- Doctores --------------------$
-- #-------------------- Agregar Doctor --------------------#

Delimiter $$
	Create procedure sp_AgregarDoctor(in numeroColegiado int, in nombresDoctor varchar(50), in apellidosDoctor varchar(50), 
							in telefonoContacto int, in codigoEspecialidad int)
		Begin
			Insert into Doctores (numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad)
				value (numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad);
		End $$
Delimiter ;

call sp_AgregarDoctor(101,'Richard','Ramirez','50201463',2);
call sp_AgregarDoctor(102,'Carlos','Alberto','45212020',3);
call sp_AgregarDoctor(103,'Mario','Casas','54632511',4);
call sp_AgregarDoctor(104,'Jonathan','Juarez','14143025',2);
call sp_AgregarDoctor(105,'Martin','Landivar','20212526',5);
call sp_AgregarDoctor(106,'Gabriel','Cordoba','10203063',2);
call sp_AgregarDoctor(107,'Carlos','Cucul','25369887',3);
call sp_AgregarDoctor(108,'Dylan','Casas','63360101',2);
call sp_AgregarDoctor(109,'Adrian','Donis','10002536',4);
call sp_AgregarDoctor(110,'Karlos','Salazar','20203030',2);
call sp_AgregarDoctor(111,'Fernando','Salazar','30251415',3);
-- #-------------------- Listar Doctor --------------------#
Delimiter $$
	Create procedure sp_ListarDoctores()
		Begin
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoContacto,
                D.codigoEspecialidad
				from Doctores D;
		End$$
Delimiter ;
call sp_ListarDoctores();

-- #-------------------- Buscar Doctor --------------------#
Delimiter $$
	Create procedure sp_BuscarDoctor(in codDoc int)
		Begin 
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoContacto,
                D.codigoEspecialidad
				from Doctores D where codDoc=numeroColegiado;
		End$$
Delimiter ;
call sp_BuscarDoctor(101);

-- #-------------------- Eliminar Doctor --------------------#
Delimiter $$
	Create procedure sp_EliminarDoctor(in codDoc int)
		Begin
			Delete from Doctores 
			where codDoc=numeroColegiado;
		End $$
Delimiter ;
call sp_EliminarDoctor(103);

-- #-------------------- Editar Doctor --------------------#
Delimiter $$
	Create procedure sp_EditarDoctor(in numColeg int, in nombreDoc varchar(50), in apellidoDoc varchar(50), in telDoc int)
		Begin 
			Update Doctores D set
				D.nombresDoctor = nombreDoc,
				D.apellidosDoctor = apellidoDoc,
				D.telefonoContacto = telDoc
					where numColeg=Numerocolegiado;
		End$$
Delimiter ;
call sp_EditarDoctor(101,'Richard','Ramírez','50201463');

-- $-------------------- Recetas --------------------$
-- #-------------------- Agregar Receta --------------------#

Delimiter $$
	Create procedure sp_AgregarReceta(in fechaReceta date, numeroColegiado int)
		Begin
			Insert into Recetas (fechaReceta,numeroColegiado)
				value (fechaReceta,numeroColegiado);
		End$$
Delimiter ;
call sp_AgregarReceta('2022-08-23',101);
call sp_AgregarReceta('2022-05-30',108);
call sp_AgregarReceta('2022-05-30',104);
call sp_AgregarReceta('2022-03-30',105);
call sp_AgregarReceta('2022-04-30',106);
call sp_AgregarReceta('2022-05-30',101);
call sp_AgregarReceta('2022-01-20',102);
call sp_AgregarReceta('2022-11-25',102);
call sp_AgregarReceta('2022-12-25',102);
call sp_AgregarReceta('2022-04-10',102);
call sp_AgregarReceta('2022-03-15',102);


-- #-------------------- Listar Receta --------------------#
Delimiter $$
	Create procedure sp_ListarRecetas()
		Begin	
			Select 
				R.codigoReceta,
                R.fechaReceta,
                R.numeroColegiado
				from Recetas R;
        End$$
Delimiter ;
call sp_ListarRecetas();

-- #-------------------- Buscar Receta --------------------#
Delimiter $$
	Create procedure sp_BuscarReceta(in codRec int)
		Begin
			Select 
				R.codigoReceta,
                R.fechaReceta,
                R.numeroColegiado
					from Recetas R where codRec=codigoReceta;
        End $$
Delimiter ;
call sp_BuscarReceta(1);

-- #-------------------- Eliminar Receta --------------------#
Delimiter $$
	Create procedure sp_EliminarReceta(in codRec int)
		Begin 
			Delete from Recetas 
				where codRec=codigoReceta;
        End$$
Delimiter ;
call sp_EliminarReceta(2);
call sp_ListarRecetas();

-- #-------------------- Editar Receta --------------------#
Delimiter $$
	Create procedure sp_EditarReceta(in codRec int, in fechaRec date)
		Begin 
			Update Recetas R set 
				R.fechaReceta = fechaRec
					where codRec = codigoReceta;
		End$$
Delimiter ;
call sp_EditarReceta(1,'2021-08-25');
-- ---
-- $-------------------- Detalle Receta --------------------$
-- #-------------------- Agregar DetalleReceta --------------------#

Delimiter $$
	Create procedure sp_AgregarDetalleReceta(in dosis varchar(100), in codigoReceta int, in codigoMedicamento int)
		Begin
			Insert into DetalleReceta (dosis,codigoReceta,codigoMedicamento)
				value (dosis,codigoReceta,codigoMedicamento);
		End$$
Delimiter ;
call sp_AgregarDetalleReceta('Una cucharada cada 8h',1,1);
call sp_AgregarDetalleReceta('Una cucharada cada 8h',1,1);
call sp_AgregarDetalleReceta('Cada 8h, 2 dosis',3,3);
call sp_AgregarDetalleReceta('Cada 12h, 1 dosis',4,4);
call sp_AgregarDetalleReceta('Cada 8h, 3 dosis',5,5);
call sp_AgregarDetalleReceta('Cada 12h, 4 dosis',6,6);
call sp_AgregarDetalleReceta('Cada 24h, 2 dosis',7,7);
call sp_AgregarDetalleReceta('Cada 6h, 3 dosis',8,8);
call sp_AgregarDetalleReceta('Cada 4h, 1 dosis',9,9);
call sp_AgregarDetalleReceta('Cada 12h, 1 dosis',10,10);



call sp_ListarRecetas();
call sp_ListarMedicamentos();

-- #-------------------- Listar DetalleReceta --------------------#
Delimiter $$
	Create procedure sp_ListarDetalleRecetas()
		Begin
			Select
				D.codigoDetalleReceta,
                D.dosis,
                D.codigoReceta,
                D.codigoMedicamento
				from DetalleReceta D;
        End$$
Delimiter ;
call sp_ListarDetalleRecetas();

-- #-------------------- Buscar DetalleReceta --------------------#
Delimiter $$
	Create procedure sp_BuscarDetalleReceta(in codDReceta int)
		Begin
			Select
				D.codigoDetalleReceta,
                D.dosis,
                D.codigoReceta,
                D.codigoMedicamento
				from DetalleReceta D where codDReceta=codigoDetalleReceta;
        End$$
Delimiter ;
call sp_BuscarDetalleReceta(1);

-- #-------------------- Eliminar DetalleReceta --------------------#
Delimiter $$
	Create procedure sp_EliminarDetalleReceta(in codDRec int)
		Begin
			Delete from DetalleReceta
				where codDRec=codigoDetalleReceta;
        End$$
Delimiter ;
call sp_EliminarDetalleReceta(2);
call sp_ListarDetalleRecetas();

-- #-------------------- Editar DetalleReceta --------------------#
Delimiter $$
	Create procedure sp_EditarDetalleReceta(in codDRec int, in doss varchar(100))
		Begin
			Update DetalleReceta D
				set
					D.dosis = doss
						where codDRec = codigoDetalleReceta;
        End $$
Delimiter ;
call sp_EditarDetalleReceta(1,'Una cucharada cada 12 horas');

-- $-------------------- Citas --------------------$
-- #-------------------- Agregar Cita --------------------#

Delimiter $$
	Create procedure sp_AgregarCita(in fechaCita date, in horaCita time, in tratamiento varchar(150), in descripCondActual varchar(255), 
		in codigoPaciente int, in numeroColegiado int)
		Begin
			Insert into Citas (fechaCita, horaCita, tratamiento, descripCondActual, codigoPaciente, numeroColegiado)
				value (fechaCita, horaCita, tratamiento, descripCondActual, codigoPaciente, numeroColegiado);
		End$$
Delimiter ;
call sp_AgregarCita('2022-03-20','12:30','x','Dolor de molares',1002,101);
call sp_AgregarCita('2022-03-20','12:30','x','Dolor de molares',1002,101);
call sp_AgregarCita('2022-04-10','14:00','x','Dolor de ensilla',1002,104);
call sp_AgregarCita('2022-03-20','15:30','x','Dolor de muela',1002,102);
call sp_AgregarCita('2022-03-10','12:30','Braquets','x',1005,105);
call sp_AgregarCita('2022-03-19','12:50','x','Dolor de muela',1007,107);
call sp_AgregarCita('2022-03-15','01:30','x','Dolor de muela',1004,107);
call sp_AgregarCita('2022-03-18','02:30','x','Dolor de ensilla',1005,107);
call sp_AgregarCita('2022-03-21','04:30','x','Caries',1006,108);
call sp_AgregarCita('2022-03-23','06:30','x','Caries',1007,108);
call sp_AgregarCita('2022-04-23','08:30','x','Caries',1009,108);


-- #-------------------- Listar Cita --------------------#
Delimiter $$
	Create procedure sp_ListarCitas()
		Begin
			Select 
				C.codigoCita,
				C.fechaCita, 
                C.horaCita, 
                C.tratamiento, 
                C.descripCondActual, 
                C.codigoPaciente, 
                C.numeroColegiado
				from Citas C;
        End$$
Delimiter ;
call sp_ListarCitas();

-- #-------------------- Buscar Cita --------------------#
Delimiter $$
	Create procedure sp_BuscarCita(in codCit int)
		Begin
			Select 
				C.codigoCita,
				C.fechaCita, 
                C.horaCita, 
                C.tratamiento, 
                C.descripCondActual, 
                C.codigoPaciente, 
                C.numeroColegiado
				from Citas C where codCit = codigoCita;
        End$$
Delimiter ;
call sp_BuscarCita(1);

-- #-------------------- Eliminar Cita --------------------#
Delimiter $$
	Create procedure sp_EliminarCita(in codCit int)
		Begin
			Delete from Citas
				where codCit = codigoCita;
		End$$
Delimiter ;
call sp_EliminarCita(2);
call sp_ListarCitas();

-- #-------------------- Editar Cita --------------------#
Delimiter $$
	Create procedure sp_EditarCita(in codCit int, in fechaCit date, in horaCit time, in tratamient varchar(150), in descripcionC varchar(255) )
			Begin
				Update Citas C set
					C.fechaCita = fechaCit, 
					C.horaCita = horaCit, 
					C.tratamiento = tratamient, 
					C.descripCondActual = descripcionC
						where codCit = codigoCita;
            End$$
Delimiter ;
Call sp_EditarCita ('1','2022-03-20','12:45:00','x','Dolor muscular');

-- #-------------------------------------------------------------------------------------------------------------------------------------------------#
-- 	
Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);

-- ---------------------------------------- AGREGAR USUARIO ------------------------------------
Delimiter $$
	Create procedure sp_AgregarUsuario (in nombreUsuario varchar(100), in apellidoUsuario varchar(100),
		in usuarioLogin varchar(50), in contrasena varchar(50) )
        Begin
			insert into Usuario(codigoUsuario, nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
				values (codigoUsuario, nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
        End $$
Delimiter ;

call sp_AgregarUsuario('Alessandro', 'Gutierrez','alexs','123');

-- ---------------------------------------- LISTAR USUARIO ------------------------------------
Delimiter $$
	Create procedure sp_ListarUsuarios()
    Begin
		Select
			U.codigoUsuario, 
			U.nombreUsuario, 
            U.apellidoUsuario, 
            U.usuarioLogin, 
            U.contrasena
        from Usuario U;
    End $$
Delimiter ; 
call sp_ListarUsuarios();
-- ---------------------------------------- Login ------------------------------------
Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);

Select * from Doctores D INNER JOIN Especialidades E on
	D.codigoEspecialidad = E.codigoEspecialidad;

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456789';
