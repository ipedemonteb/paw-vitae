import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { createMemoryRouter, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { createMockDoctor } from '@/__test__/utils/factories';
import PublicProfile from "@/pages/PublicProfile.tsx";
import {BASE_URL} from "@/__test__/utils/utils.ts";



const mockUseDoctor = vi.fn();
const mockUseDoctorSpecialties = vi.fn();
const mockUseDoctorCoverages = vi.fn();
const mockUseDoctorExperience = vi.fn();
const mockUseDoctorCertifications = vi.fn();
const mockUseDoctorBiography = vi.fn();
const mockUseRatings = vi.fn();
const mockUseDoctorOffices = vi.fn();
const mockUseDoctorImageUrl = vi.fn();
const mockUseAuth = vi.fn();

vi.mock('@/hooks/useDoctors', () => ({
    useDoctor: (id: string) => mockUseDoctor(id),
    useDoctorSpecialties: () => mockUseDoctorSpecialties(),
    useDoctorCoverages: () => mockUseDoctorCoverages(),
    useDoctorExperience: () => mockUseDoctorExperience(),
    useDoctorCertifications: () => mockUseDoctorCertifications(),
    useDoctorBiography: () => mockUseDoctorBiography(),
    useDoctorImageUrl: () => mockUseDoctorImageUrl(),
    useUpdateDoctorProfileMutation: () => ({ mutate: vi.fn() }),
    useUpdateDoctorCertificatesMutation: () => ({ mutate: vi.fn() }),
    useUpdateDoctorExperienceMutation: () => ({ mutate: vi.fn() }),
}));

vi.mock('@/hooks/useRatings', () => ({
    useRatings: () => mockUseRatings()
}));

vi.mock('@/hooks/useOffices', () => ({
    useDoctorOffices: () => mockUseDoctorOffices()
}));

vi.mock('@/hooks/useNeighborhoods', () => ({
    useNeighborhood: () => ({ data: { name: 'Palermo' }, isLoading: false })
}));

vi.mock('@/hooks/useAuth', () => ({
    useAuth: () => mockUseAuth()
}));


vi.mock("@/utils/queryUtils.ts", () => ({
    useDelayedBoolean: (val: boolean) => val
}));

vi.mock("@/utils/IdUtils.ts", () => ({
    userIdFromSelf: () => "1"
}));

vi.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key: string, defaultValue?: string) => {
            if (key === 'doctor.profile.card.edit') return 'Edit';
            if (key === 'doctor.profile.schedule') return 'Schedule Appointment';
            return defaultValue || key;
        }
    })
}));

vi.mock('@/components/ui/carousel', () => ({
    Carousel: ({ children }: any) => <div>{children}</div>,
    CarouselContent: ({ children }: any) => <div>{children}</div>,
    CarouselItem: ({ children }: any) => <div>{children}</div>,
    CarouselNext: () => null,
    CarouselPrevious: () => null,
}));

const renderProfile = (id = '1') => {
    const queryClient = new QueryClient({ defaultOptions: { queries: { retry: false } } });
    const router = createMemoryRouter([
        { path: '/profile/:id', element: <PublicProfile /> }
    ], { initialEntries: [`/profile/${id}`] });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
        </QueryClientProvider>
    );
};

describe('Public Profile Page (Mocked Hooks)', () => {

    const doctorData = createMockDoctor({
        name: 'Gregory',
        lastName: 'House',
        email: 'house@princeton.edu',
        phone: '555-0199'
    });

    const setupSuccessState = () => {
        mockUseDoctor.mockReturnValue({ data: doctorData, isLoading: false, isError: false });
        mockUseDoctorSpecialties.mockReturnValue({ data: [{ name: 'Infectious Disease' }], isLoading: false });
        mockUseDoctorCoverages.mockReturnValue({ data: [{ name: 'Medicare' }], isLoading: false });
        mockUseDoctorExperience.mockReturnValue({ data: [{ positionTitle: 'Head of Dept', organizationName: 'Princeton' }], isLoading: false });
        mockUseDoctorCertifications.mockReturnValue({ data: [{ certificateName: 'Board Certified', issuingEntity: 'Board', issueDate: '1990-01-01' }], isLoading: false });
        mockUseDoctorBiography.mockReturnValue({ data: { bio: 'Expert Bio', description: 'Expert Desc' }, isLoading: false });
        mockUseRatings.mockReturnValue({ data: [], isLoading: false });
        mockUseDoctorOffices.mockReturnValue({ data: [], isLoading: false });
        mockUseDoctorImageUrl.mockReturnValue({ url: `${BASE_URL}/doctors/1/image`, isLoading: false });
        mockUseAuth.mockReturnValue({ email: 'visitor@test.com' });
    };

    beforeEach(() => {
        vi.clearAllMocks();
        setupSuccessState();
    });

    it('should show loading state initially', () => {
        mockUseDoctor.mockReturnValue({ data: undefined, isLoading: true });

        renderProfile();

        expect(screen.queryByText('Gregory House')).not.toBeInTheDocument();
    });

    it('should render doctor information correctly', () => {
        renderProfile();

        expect(screen.getByText('Gregory House')).toBeInTheDocument();
        expect(screen.getByText('house@princeton.edu')).toBeInTheDocument();
        expect(screen.getByText('555-0199')).toBeInTheDocument();
        expect(screen.getByText('Expert Bio')).toBeInTheDocument();
        expect(screen.getByText('Infectious Disease')).toBeInTheDocument();
        expect(screen.getByText('Medicare')).toBeInTheDocument();
        expect(screen.getByText('Head of Dept')).toBeInTheDocument();
    });

    it('should show "Schedule Appointment" button for visitors', () => {
        mockUseAuth.mockReturnValue({ email: 'visitor@test.com' });
        renderProfile();

        expect(screen.getByText('Schedule Appointment')).toBeInTheDocument();
        expect(screen.queryByText('Edit')).not.toBeInTheDocument();
    });

    it('should show "Edit" buttons for the Owner', () => {
        mockUseAuth.mockReturnValue({ email: 'house@princeton.edu' });
        renderProfile();

        expect(screen.queryByText('Schedule Appointment')).not.toBeInTheDocument();
        expect(screen.getAllByText('Edit').length).toBeGreaterThan(0);
    });

    it('should render 404 error page when doctor not found', () => {
        mockUseDoctor.mockReturnValue({ data: undefined, isLoading: false, isError: true });

        renderProfile();

        expect(screen.getByText(/404/i)).toBeInTheDocument();
    });
});