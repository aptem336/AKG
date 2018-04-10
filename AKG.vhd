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

--���������� �� ������
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
	--��������� ��������
	VARIABLE a, x : UNSIGNED (31 downto 0);
	--���� �������
	VARIABLE s1, s2 : POSITIVE;
	--��������� �����
	VARIABLE random : REAL;

begin 
--��������� a
UNIFORM(s1, s2, random);
--�����������
a := to_unsigned(INTEGER(random * REAL(2147483647)), 32);
--x = a ^ t mon n
x := MOD_3(a, t, n);
--���� 1 ��� -1 �� ���� ����, �� �� ��������� (���������� � �������) ����� ������ 1
if (x = 1 OR x = -1) then
	return '1';
end if;
--�������� � ������� ������� ���, �������� ����� ������� ������
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
--����� �� n - 1 => ���������
return '0';
end MILLER_RABIN;

function TEST_REPEATER (n : UNSIGNED (31 downto 0); k : INTEGER) return STD_LOGIC is
	--�������� ��������� n - 1
	VARIABLE t : UNSIGNED (31 downto 0);	
	--������� ������
	VARIABLE s : INTEGER;
	
begin
--������������ n - 1
--n - 1 = 2 ^ s * t
t := n - 1;
--������? (������� ��� = 0)
while t MOD 2 = 0 loop
	--����� �� ��� (�������� ������)
	t := t srl 1;
	--����������� ������� (������� ��� �������� �� 2 = ������� ������ � ����������)
	s := s + 1;
end loop;

for i in 1 to k loop
	--�� ������ ���� �� ��� - ���������
	if (MILLER_RABIN(n, t, s) = '0') then
		return '0';
	end if;	
end loop;
--������ � ��� - �������
return '1';
end TEST_REPEATER;

begin
	bool<=TEST_REPEATER(to_unsigned(2921091883, 32), 5);
	qT<=STD_LOGIC_VECTOR(to_unsigned(2921091883, 32));
end PAA;