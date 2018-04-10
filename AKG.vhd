library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;
use ieee.MATH_REAL.all;

entity AKG is
port(
	qT : out std_logic_vector (31 downto 0);
	bool : out std_logic
);
end AKG;  

architecture PAA of AKG is

--возведение по модулю
function MOD_3 (aT, bT, c : UNSIGNED (31 downto 0)) return UNSIGNED is
	
VARIABLE x : UNSIGNED (31 downto 0) := TO_UNSIGNED(1, 32);
VARIABLE a : UNSIGNED (31 downto 0) := aT;
VARIABLE b : UNSIGNED (31 downto 0) := bT;

begin
while b > 0 loop
	if b MOD 2 = 1 then
		x := (x * a) MOD c;
		a := (a * a) MOD c;
		b := (b - 1) / 2;
	else 
		a := (a * a) MOD c;
		b := b / 2;
	end if;
end loop;
return x MOD c;
end MOD_3 ;

function MILLER_RABIN (n, t : UNSIGNED (31 downto 0); s : INTEGER) return STD_LOGIC is
	--свидетель простоты
	VARIABLE a, x : UNSIGNED (31 downto 0);
	--семя рандома
	VARIABLE s1, s2 : POSITIVE;
	--рандомное число
	VARIABLE random : REAL;

begin 
--рандомное a
UNIFORM(s1, s2, random);
--преобразуем
a := to_unsigned(INTEGER(random * REAL(2147483647)), 32);
--x = a ^ t mon n
x := MOD_3(a, t, n);
--если 1 или -1 на этом шаге, то на следующем (возведении к квадрат) будет равным 1
if (x = 1 OR x = -1) then
	return '1';
end if;
--возводим в квадрат столько раз, скольким равна степень двойки
for i in 1 to s - 1 loop
	x := (x * x) MOD n;
	--
	if (x = 1) then
		return '0';
	end if;
	--
	if (x = -1) then
		return '1';
	end if;
end loop;
--дошли до n - 1 => составное
return '0';
end MILLER_RABIN;

function TEST_REPEATER (n : UNSIGNED (31 downto 0); k : INTEGER) return STD_LOGIC is
	--нечетный множитель n - 1
	VARIABLE t : UNSIGNED (31 downto 0);	
	--степень двойки
	VARIABLE s : INTEGER;
	
begin
--раскладываем n - 1
--n - 1 = 2 ^ s * t
t := n - 1;
--четное? (младший бит = 0)
while t MOD 2 = 0 loop
	--делим на два (сдвигаем вправо)
	t := t srl 1;
	--увеличиваем счётчик (сколько раз делиться на 2 = степень двойки в разложении)
	s := s + 1;
end loop;

for i in 1 to k loop
	--не прошёл хотя бы раз - составное
	if (MILLER_RABIN(n, t, s) = '0') then
		return '0';
	end if;	
end loop;
--прошёл к раз - простое
return '1';
end TEST_REPEATER;

begin
	bool<=TEST_REPEATER(to_unsigned(2921091883, 32), 5);
	qT<=STD_LOGIC_VECTOR(to_unsigned(2921091883, 32));
end PAA;