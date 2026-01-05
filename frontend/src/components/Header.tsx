import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuItem } from "@/components/ui/dropdown-menu"
import { NavigationMenu, NavigationMenuList, NavigationMenuItem, NavigationMenuLink } from "@/components/ui/navigation-menu"
import { Sheet, SheetTrigger, SheetContent, SheetClose } from "@/components/ui/sheet"
import { ChevronDown, User, BriefcaseMedical, Menu, LogIn } from "lucide-react";
import { Link, useMatch } from "react-router-dom";
import { cn } from "@/lib/utils";
import React, { useEffect, useState } from "react";

function HeaderNavLink({ to, end, children, }: {
    to: string;
    end?: boolean;
    children: React.ReactNode;
}) {
    const match = useMatch({ path: to, end: end });
    return (
        <NavigationMenuLink asChild className={cn(navItem, match && navItemActive)}>
            <Link to={to}>{children}</Link>
        </NavigationMenuLink>
    );
}

function SheetNavLink({ to, end, children }: {
    to: string;
    end?: boolean;
    children: React.ReactNode;
}) {
    const match = useMatch({ path: to, end: end });
    return (
        <SheetClose asChild>
            <Link to={to} className={cn(mobileNavLink, match && mobileNavLinkActive)}>
                {children}
            </Link>
        </SheetClose>
    );
}

const header =
    "fixed top-0 left-0 w-full shadow-[var(--shadow-md)] py-8 leading-[1.6] bg-white z-50";
const headerContainer =
    "flex items-center justify-between w-full max-w-6xl px-5 mx-auto";
const logo =
    "flex items-center text-[var(--primary-color)] transition-transform duration-300 ease-[ease] hover:scale-105";
const nav =
    "font-medium";
const navDesktop =
    "hidden md:block";
const navList =
    "flex gap-8 items-center";
const navItem =
    "text-base cursor-pointer hover:text-[var(--primary-color)] hover:bg-transparent " +
    "relative px-0 py-1 font-medium transition-colors duration-300 " +
    "focus:bg-transparent active:bg-transparent focus:text-[var(--primary-color)] " +
    "after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-0.5 after:w-0 after:bg-[var(--primary-color)] " +
    "after:transition-[width] after:duration-300 hover:after:w-full";
const navItemActive =
    "text-[var(--primary-color)] font-semibold after:w-full";
const btnBase =
    "inline-flex items-center justify-center text-base font-medium leading-[1.5] px-6 h-11 rounded-md !border-2 transition-colors transition-transform duration-300 ease-in-out";
const btnOutline =
    `${btnBase} bg-transparent text-[var(--primary-color)] border-[var(--primary-color)] hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] hover:text-[var(--white)]`;
const btnFilled =
    `${btnBase} bg-[var(--primary-color)] text-[var(--white)] border-[var(--primary-color)] hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer`;
const dropDownItem =
    "flex items-center gap-2 text-base cursor-pointer data-[highlighted]:text-[var(--primary-color)] data-[highlighted]:bg-transparent";
const authDesktop =
    "hidden md:flex gap-3 items-center";

function Header() {
    const [sheetOpen, setSheetOpen] = useState(false);

    useEffect(() => {
        if (typeof window === "undefined") return;

        const mql = window.matchMedia("(min-width: 768px)");

        const onChange = (e: MediaQueryListEvent) => {
            if (e.matches) setSheetOpen(false);
        };

        if (mql.matches) setSheetOpen(false);

        const mqlAny = mql as unknown as {
            addEventListener?: (type: "change", listener: (e: MediaQueryListEvent) => void) => void;
            removeEventListener?: (type: "change", listener: (e: MediaQueryListEvent) => void) => void;
            addListener?: (listener: (e: MediaQueryListEvent) => void) => void;
            removeListener?: (listener: (e: MediaQueryListEvent) => void) => void;
        };

        if (mqlAny.addEventListener && mqlAny.removeEventListener) {
            mqlAny.addEventListener("change", onChange);
            return () => mqlAny.removeEventListener!("change", onChange);
        }

        mqlAny.addListener?.(onChange);
        return () => mqlAny.removeListener?.(onChange);
    }, []);

    return (
        <div className={header}>
            <div className={headerContainer}>
                <a href="/" className={logo}>
                    <h1 className="block text-5xl font-bold no-underline">Vitae</h1>
                </a>
                <NavigationMenu className={cn(nav, navDesktop)}>
                    <NavigationMenuList className={navList}>
                        <NavigationMenuItem>
                            <HeaderNavLink to="/" end>
                                Home
                            </HeaderNavLink>
                        </NavigationMenuItem>
                        <NavigationMenuItem>
                            <HeaderNavLink to="/search">
                                Find Doctors
                            </HeaderNavLink>
                        </NavigationMenuItem>
                        <NavigationMenuItem>
                            <HeaderNavLink to="/about-us">
                                About Us
                            </HeaderNavLink>
                    </NavigationMenuItem>
                </NavigationMenuList>
            </NavigationMenu>
            <div className={authDesktop}>
                <Link to="/login" className={btnOutline}>
                    Login
                </Link>
                <DropdownMenu>
                    <DropdownMenuTrigger className={btnFilled}>
                        Register
                        <ChevronDown className="h-4 w-4" />
                    </DropdownMenuTrigger>
                    <DropdownMenuContent>
                        <DropdownMenuItem className={dropDownItem}>
                            <User className="text-inherit" />
                            Register
                        </DropdownMenuItem>
                        <DropdownMenuItem className={dropDownItem}>
                            <BriefcaseMedical className="text-inherit" />
                            Are you a Doctor?
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            </div>

            <SheetComponent open={sheetOpen} onOpenChange={setSheetOpen} />
        </div>
</div>
);
}

const mobileTrigger =
    "md:hidden inline-flex items-center justify-center h-11 w-11 rounded-md border border-[var(--primary-color)] text-[var(--primary-color)] hover:text-white hover:border-[var(--primary-dark)] hover:bg-[var(--primary-dark)] cursor-pointer";
const sheetContent =
    "w-screen max-w-none p-6 border-r-0";
const mobileNav =
    "mt-6 flex flex-col gap-4";
const mobileNavLink =
    "text-base cursor-pointer hover:text-[var(--primary-color)] hover:bg-transparent " +
    "relative w-full px-0 py-1 font-medium transition-colors duration-300 " +
    "after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-0.5 after:w-0 after:bg-[var(--primary-color)] " +
    "after:transition-[width] after:duration-300 hover:after:w-full";
const mobileNavLinkActive =
    "text-[var(--primary-color)] font-semibold after:w-full";
const mobileSectionTitle =
    "mt-2 text-sm font-semibold text-[var(--text-light)]";
const mobileIconContainer =
    "flex items-center gap-1";
const mobileIcon =
    "h-5 w-5";

function SheetComponent({ open, onOpenChange }: {
    open: boolean;
    onOpenChange: (open: boolean) => void;
}) {
    return (
        <Sheet open={open} onOpenChange={onOpenChange}>
            <SheetTrigger className={mobileTrigger} aria-label="Open menu">
                <Menu className="h-5 w-5" />
            </SheetTrigger>
            <SheetContent side="left" className={sheetContent}>
                <nav className={mobileNav}>
                    <SheetNavLink to="/" end>
                        Home
                    </SheetNavLink>
                    <SheetNavLink to="/search">
                        Find Doctors
                    </SheetNavLink>
                    <SheetNavLink to="/about-us">
                        About Us
                    </SheetNavLink>
                    <div className={mobileSectionTitle}>Account</div>
                    <SheetNavLink to="/login">
                        <div className={mobileIconContainer}>
                            <LogIn className={mobileIcon}/>
                            Login
                        </div>
                    </SheetNavLink>
                    <SheetNavLink to="/register">
                        <div className={mobileIconContainer}>
                            <User className={mobileIcon}/>
                            Register
                        </div>
                    </SheetNavLink>
                    <SheetNavLink to="/register">
                        <div className={mobileIconContainer}>
                            <BriefcaseMedical className={mobileIcon}/>
                            Are You a Doctor?
                        </div>
                    </SheetNavLink>
                </nav>
            </SheetContent>
        </Sheet>
    )
}

export default Header;
